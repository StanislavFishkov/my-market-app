package ru.yandex.practicum.mymarket.service.item;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

public interface ItemService {
    Mono<ItemDto> getItemById(Long itemId);

    Mono<Page<ItemDto>> findItems(ItemSearchRequestDto itemSearchRequestDto);

    Flux<ItemDto> findCartItems();

    Mono<Void> changeItemCount(Long itemId, CartItemAction cartItemAction);
}