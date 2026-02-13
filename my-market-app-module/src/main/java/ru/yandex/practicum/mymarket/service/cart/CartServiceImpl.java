package ru.yandex.practicum.mymarket.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.cart.CartItem;
import ru.yandex.practicum.mymarket.repository.cart.CartItemRepository;
import ru.yandex.practicum.mymarket.service.item.ItemCacheService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ItemCacheService itemCacheService;
    private final CartItemRepository cartItemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Flux<ItemWithCountDto> findCartItems() {
        return cartItemRepository.findAll()
                .flatMap(cartItem -> itemCacheService.getItemById(cartItem.getItemId())
                        .map(itemDto -> itemMapper.toDto(itemDto, cartItem.getCount()))
                );
    }

    @Override
    @Transactional
    public Mono<Void> changeItemCount(Long itemId, CartItemAction cartItemAction) {
        return cartItemRepository.findByItemId(itemId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(CartItem.builder()
                                .itemId(itemId)
                                .build()))
                )
                .flatMap(existing -> {
                    int count = existing.getCount();

                    switch (cartItemAction) {
                        case PLUS -> existing.setCount(count + 1);
                        case MINUS -> existing.setCount(Math.max(0, count - 1));
                        case DELETE -> existing.setCount(0);
                        default -> {
                            return Mono.error(new IllegalStateException("Unexpected value: " + cartItemAction));
                        }
                    }

                    if (existing.getCount() > 0) {
                        return cartItemRepository.save(existing);
                    } else if (existing.getId() != null) {
                        return cartItemRepository.delete(existing);
                    } else {
                        return Mono.empty();
                    }
                })
                .then();
    }

    @Override
    public Mono<Void> deleteAllCartItems() {
        return cartItemRepository.deleteAll();
    }
}
