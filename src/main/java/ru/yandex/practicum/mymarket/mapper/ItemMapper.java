package ru.yandex.practicum.mymarket.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.model.Item;

@Mapper
public interface ItemMapper {
    ItemDto toDto(Item item);
}