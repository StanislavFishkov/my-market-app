package ru.yandex.practicum.mymarket.mapper.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;
import ru.yandex.practicum.mymarket.dto.order.OrderItemDto;
import ru.yandex.practicum.mymarket.model.order.Order;
import ru.yandex.practicum.mymarket.model.order.OrderItem;

@Mapper
public interface OrderMapper {
    OrderDto toDto(Order order);

    @Mapping(target = "id", source = "itemId")
    OrderItemDto toDto(OrderItem orderItem);
}