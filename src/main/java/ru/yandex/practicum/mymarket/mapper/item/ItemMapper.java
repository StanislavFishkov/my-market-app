package ru.yandex.practicum.mymarket.mapper.item;

import org.mapstruct.Mapper;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.model.item.Item;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemDto toDto(Item item);

    List<ItemDto> toDto(List<Item> item);
}