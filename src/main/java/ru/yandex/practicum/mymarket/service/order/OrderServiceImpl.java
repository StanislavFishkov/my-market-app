package ru.yandex.practicum.mymarket.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.mapper.order.OrderMapper;
import ru.yandex.practicum.mymarket.model.order.Order;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.repository.order.OrderItemRepository;
import ru.yandex.practicum.mymarket.repository.order.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public Mono<Long> createOrder() {
        return itemRepository.findAllByCountGreaterThan(0)
                .collectList()
                .flatMap(cartItems -> {
                    if (cartItems.isEmpty()) {
                        return Mono.error(new NotFoundException("Cart items not found"));
                    }

                    return orderRepository.save(new Order())
                            .flatMap(order -> {
                                order.setItems(itemMapper.toOrderItems(cartItems, order.getId()));
                                return orderItemRepository.saveAll(order.getItems())
                                        .then(Mono.just(order.getId()));
                            })
                            .flatMap(orderId -> {
                                cartItems.forEach(cartItem -> cartItem.setCount(0));
                                return itemRepository.saveAll(cartItems)
                                        .then(Mono.just(orderId));

                            })
                            .doOnNext(orderId -> log.debug("Order created: id={}", orderId));
                });
    }

    @Override
    public Mono<OrderDto> getOrderById(Long orderId) {
        log.debug("Order requested: id={}", orderId);

        return getOrderOrThrow(orderId)
                .map(orderMapper::toDto);
    }

    @Override
    public Flux<OrderDto> findOrders() {
        log.debug("Orders requested");

        return orderRepository.findAll()
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
}