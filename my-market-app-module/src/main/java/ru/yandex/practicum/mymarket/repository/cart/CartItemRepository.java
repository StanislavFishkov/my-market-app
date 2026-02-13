package ru.yandex.practicum.mymarket.repository.cart;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.cart.CartItem;

import java.util.List;

public interface CartItemRepository extends ReactiveCrudRepository<CartItem, Long> {
    Mono<CartItem> findByItemId(Long itemId);

    Flux<CartItem> findAllByItemIdIn(List<Long> itemIds);
}