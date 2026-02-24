package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.cart.CartItem;
import ru.yandex.practicum.mymarket.repository.cart.CartItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemCacheService itemCacheService;
    private final CartItemRepository cartItemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Mono<ItemWithCountDto> getItemById(Long userId, Long itemId) {
        log.debug("Item requested: id={}", itemId);

        return itemCacheService.getItemById(itemId)
                .flatMap(item -> cartItemRepository
                        .findByUserIdAndItemId(userId, itemId)
                        .map(CartItem::getCount)
                        .defaultIfEmpty(0)
                        .map(count -> itemMapper.toDto(item, count))
                );
    }

    @Override
    public Mono<Page<ItemWithCountDto>> findItems(Long userId, ItemSearchRequestDto itemSearchRequestDto) {
        log.debug("Items requested: criteria={}", itemSearchRequestDto);

        return itemCacheService.findItems(itemSearchRequestDto)
                .flatMap(pageDto -> {
                    var page = new PageImpl<>(pageDto.getContent(), itemSearchRequestDto.toPageable(), pageDto.getTotal());

                    if (userId != null) {
                        return cartItemRepository.findAllByUserIdAndItemIdIn(userId, page.getContent().stream().map(ItemDto::getId).toList())
                                .collectMap(CartItem::getItemId, CartItem::getCount)
                                .map(cartItemMap -> page.map(
                                        itemDto ->
                                                itemMapper.toDto(itemDto, cartItemMap.getOrDefault(itemDto.getId(), 0)))
                                );
                    } else  {
                        return Mono.just(page.map(itemDto -> itemMapper.toDto(itemDto, 0)));
                    }
                });
    }
}
