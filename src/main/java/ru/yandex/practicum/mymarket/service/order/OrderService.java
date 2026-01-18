package ru.yandex.practicum.mymarket.service.order;

import ru.yandex.practicum.mymarket.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    Long createOrder();

    OrderDto getOrderById(Long orderId);

    List<OrderDto> findOrders();
}