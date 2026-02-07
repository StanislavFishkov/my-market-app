package ru.yandex.practicum.mymarket.repository.order;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.yandex.practicum.mymarket.model.order.Order;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}