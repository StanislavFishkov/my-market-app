package ru.yandex.practicum.mymarket.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.order.OrderMapper;
import ru.yandex.practicum.mymarket.model.order.Order;
import ru.yandex.practicum.mymarket.repository.order.OrderRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Long createOrder() {
        Order order = orderRepository.save(new Order());
        log.debug("Order created: id={}", order.getId());
        return order.getId();
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        log.debug("Order requested: id={}", orderId);

        Order order = getOrderOrThrow(orderId);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findOrders() {
        log.debug("Orders requested");

        return orderMapper.toDto(orderRepository.findAll());
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order doesn't exist with id: %s".formatted(orderId)));
    }
}