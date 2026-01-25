package ru.yandex.practicum.mymarket.mapper.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.model.order.OrderItem;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemDto toDto(Item item);

    List<ItemDto> toDto(List<Item> item);

    @Mapping(target = "itemId", source = "id")
    OrderItem toOrderItem(Item item);

    List<OrderItem> toOrderItem(List<Item> item);
}