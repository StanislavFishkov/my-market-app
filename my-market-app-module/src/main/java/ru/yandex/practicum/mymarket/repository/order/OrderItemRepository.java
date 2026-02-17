package ru.yandex.practicum.mymarket.repository.order;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.mymarket.model.order.OrderItem;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    Flux<OrderItem> findAllByOrderId(Long orderId);
}