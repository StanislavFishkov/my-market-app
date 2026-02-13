package ru.yandex.practicum.mymarket.service.item;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemPageDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

public interface ItemCacheService {
    Mono<ItemDto> getItemById(Long itemId);

    Mono<ItemPageDto> findItems(ItemSearchRequestDto itemSearchRequestDto);
}