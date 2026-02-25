package ru.yandex.practicum.mymarket.service.item;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

public interface ItemService {
    Mono<ItemWithCountDto> getItemById(Long userId, Long itemId);

    Mono<Page<ItemWithCountDto>> findItems(Long userId, ItemSearchRequestDto itemSearchRequestDto);
}