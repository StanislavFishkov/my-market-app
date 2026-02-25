package ru.yandex.practicum.mymarket.service.order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;

public interface OrderService {
    Mono<Long> createOrder(Long userId);

    Mono<OrderDto> getOrderById(Long orderId);

    Flux<OrderDto> findOrders(Long userId);
}