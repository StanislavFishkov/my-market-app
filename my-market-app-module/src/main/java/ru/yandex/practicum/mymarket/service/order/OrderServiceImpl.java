package ru.yandex.practicum.mymarket.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;
import ru.yandex.practicum.mymarket.exception.InsufficientFundsException;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.exception.PaymentServiceUnavailableException;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.mapper.order.OrderMapper;
import ru.yandex.practicum.mymarket.model.order.Order;
import ru.yandex.practicum.mymarket.payment.api.PaymentApi;
import ru.yandex.practicum.mymarket.payment.dto.PaymentRequest;
import ru.yandex.practicum.mymarket.repository.order.OrderItemRepository;
import ru.yandex.practicum.mymarket.repository.order.OrderRepository;
import ru.yandex.practicum.mymarket.service.cart.CartService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final CartService cartService;

    private final PaymentApi paymentApi;

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public Mono<Long> createOrder(Long userId) {
        return cartService.findCartItems(userId)
                .collectList()
                .flatMap(cartItems -> {
                    if (cartItems.isEmpty()) {
                        return Mono.error(new NotFoundException("Cart items not found"));
                    }

                    long total = cartItems.stream().mapToLong(item -> item.getPrice() * item.getCount()).sum();

                    return paymentApi.makePayment(PaymentRequest.builder().amount(total).build())
                            .onErrorMap(ex -> {
                                if (ex instanceof WebClientResponseException webClientException) {
                                    return mapHttpStatusToException(webClientException.getStatusCode().value(), ex);
                                }

                                return new PaymentServiceUnavailableException("Payment service unavailable", ex);
                            })
                            .then(orderRepository.save(Order.builder().userId(userId).build())
                                    .flatMap(order -> {
                                        order.setItems(itemMapper.toOrderItems(cartItems, order.getId()));
                                        return orderItemRepository.saveAll(order.getItems())
                                                .then(Mono.just(order.getId()));
                                    })
                                    .flatMap(orderId -> cartService.deleteAllCartItems(userId)
                                            .then(Mono.just(orderId))
                                    )
                                    .doOnNext(orderId -> log.debug("Order created: id={}", orderId))
                            );
                });
    }

    @Override
    @PostAuthorize("returnObject.userId == authentication.principal.id")
    public Mono<OrderDto> getOrderById(Long orderId) {
        log.debug("Order requested: id={}", orderId);

        return getOrderOrThrow(orderId)
                .map(orderMapper::toDto);
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public Flux<OrderDto> findOrders(Long userId) {
        log.debug("Orders requested");

        return orderRepository.findAllByUserId(userId)
                .flatMap(this::getAndSetOrderItems)
                .map(orderMapper::toDto);
    }

    private Mono<Order> getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order doesn't exist with id: %s".formatted(orderId))))
                .flatMap(this::getAndSetOrderItems);
    }

    private Mono<Order> getAndSetOrderItems(Order order) {
        return orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .map(items -> {
                    order.setItems(items);
                    return order;
                });
    }

    private RuntimeException mapHttpStatusToException(int statusCode, Throwable ex) {
        return switch (statusCode) {
            case 400 -> new PaymentServiceUnavailableException("Bad Request", ex);
            case 422 -> new InsufficientFundsException("Not enough funds to pay", ex);
            default -> new PaymentServiceUnavailableException("Payment service unavailable: " + statusCode, ex);
        };
    }
}