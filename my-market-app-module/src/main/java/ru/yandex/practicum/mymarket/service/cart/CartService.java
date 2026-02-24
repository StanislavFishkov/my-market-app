package ru.yandex.practicum.mymarket.service.cart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartDto;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;

public interface CartService {
    Flux<ItemWithCountDto> findCartItems(Long userId);

    Mono<Void> changeItemCount(Long userId, Long itemId, CartItemAction cartItemAction);

    Mono<Void> deleteAllCartItems(Long userId);

    Mono<CartDto> getCart(Long userId);
}