package ru.yandex.practicum.mymarket.service.cart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;

public interface CartService {
    Flux<ItemWithCountDto> findCartItems();

    Mono<Void> changeItemCount(Long itemId, CartItemAction cartItemAction);

    Mono<Void> deleteAllCartItems();
}