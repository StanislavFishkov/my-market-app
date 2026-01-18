package ru.yandex.practicum.mymarket.service.item;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

public interface ItemService {
    ItemDto getItemById(Long itemId);

    Page<ItemDto> findItems(ItemSearchRequestDto itemSearchRequestDto);
}