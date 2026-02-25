package ru.yandex.practicum.mymarket.repository.cart;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.cart.CartItem;

import java.util.List;

public interface CartItemRepository extends ReactiveCrudRepository<CartItem, Long> {
    Flux<CartItem> findAllByUserId(Long userId);

    Mono<CartItem> findByUserIdAndItemId(Long userId, Long itemId);

    Flux<CartItem> findAllByUserIdAndItemIdIn(Long userId, List<Long> itemIds);

    Mono<Void> deleteAllByUserId(Long userId);
}