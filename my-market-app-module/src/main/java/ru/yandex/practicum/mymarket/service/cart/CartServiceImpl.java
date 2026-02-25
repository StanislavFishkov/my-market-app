package ru.yandex.practicum.mymarket.service.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartDto;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.cart.CartItem;
import ru.yandex.practicum.mymarket.payment.api.BalanceApi;
import ru.yandex.practicum.mymarket.payment.dto.BalanceResponse;
import ru.yandex.practicum.mymarket.repository.cart.CartItemRepository;
import ru.yandex.practicum.mymarket.service.item.ItemCacheService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ItemCacheService itemCacheService;
    private final CartItemRepository cartItemRepository;
    private final ItemMapper itemMapper;

    private final BalanceApi balanceApi;

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public Flux<ItemWithCountDto> findCartItems(Long userId) {
        return cartItemRepository.findAllByUserId(userId)
                .flatMap(cartItem -> itemCacheService.getItemById(cartItem.getItemId())
                        .map(itemDto -> itemMapper.toDto(itemDto, cartItem.getCount()))
                );
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public Mono<Void> changeItemCount(Long userId, Long itemId, CartItemAction cartItemAction) {
        return cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(CartItem.builder()
                                .userId(userId)
                                .itemId(itemId)
                                .build()))
                )
                .flatMap(existing -> {
                    existing.setCount(
                            switch (cartItemAction) {
                                case PLUS -> existing.getCount() + 1;
                                case MINUS -> Math.max(0, existing.getCount() - 1);
                                case DELETE -> 0;
                            }
                    );

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
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public Mono<Void> deleteAllCartItems(Long userId) {
        return cartItemRepository.deleteAllByUserId(userId);
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public Mono<CartDto> getCart(Long userId) {
        return cartItemRepository.findAllByUserId(userId)
                .flatMap(cartItem -> itemCacheService.getItemById(cartItem.getItemId())
                        .map(itemDto -> itemMapper.toDto(itemDto, cartItem.getCount()))
                ).collectList()
                .zipWith(balanceApi.getBalance()
                        .map(Optional::of)
                        .onErrorResume(ex -> Mono.just(Optional.empty()))
                )
                .map(tuple -> {
                    List<ItemWithCountDto> cartItems = tuple.getT1();
                    Optional<BalanceResponse> balanceResponse = tuple.getT2();

                    CartDto cartDto = new CartDto();
                    cartDto.setItems(cartItems);
                    if (balanceResponse.isPresent()) {
                        cartDto.setPaymentAvailable(true);
                        cartDto.setBalance(balanceResponse.get().getBalance());
                    }

                    return cartDto;
                });
    }
}
