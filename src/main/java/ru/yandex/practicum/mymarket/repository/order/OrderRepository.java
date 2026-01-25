package ru.yandex.practicum.mymarket.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.mymarket.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}