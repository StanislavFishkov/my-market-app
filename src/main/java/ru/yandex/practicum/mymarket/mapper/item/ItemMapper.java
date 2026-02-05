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

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "itemId", source = "item.id")
    OrderItem toOrderItem(Item item, Long orderId);

    default List<OrderItem> toOrderItems(List<Item> items, Long orderId) {
        if (items == null) {
            return List.of();
        }

        return items.stream()
                .map(item -> toOrderItem(item, orderId))
                .toList();
    }
}