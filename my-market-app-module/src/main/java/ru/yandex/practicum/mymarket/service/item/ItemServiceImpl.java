package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.repository.item.criteria.ItemCriteria;
import ru.yandex.practicum.mymarket.utils.PagingUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    @Cacheable(value = "item", key = "#itemId")
    public Mono<ItemDto> getItemById(Long itemId) {
        log.debug("Item requested: id={}", itemId);

        return getItemOrThrow(itemId)
                .map(itemMapper::toDto);
    }

    @Override
    public Mono<Page<ItemDto>> findItems(ItemSearchRequestDto itemSearchRequestDto) {
        log.debug("Items requested: criteria={}", itemSearchRequestDto);

        Criteria spec = ItemCriteria.searchInTitleOrDescription(itemSearchRequestDto.search());

        return PagingUtils.selectPage(r2dbcEntityTemplate, spec, itemSearchRequestDto.toPageable(), Item.class,
                itemMapper::toDto);
    }

    @Override
    public Flux<ItemDto> findCartItems() {
        return itemRepository.findAllByCountGreaterThan(0)
                .map(itemMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<Void> changeItemCount(Long itemId, CartItemAction cartItemAction) {
        return getItemOrThrow(itemId)
                .flatMap(existing -> {
                    int count = existing.getCount();

                    switch (cartItemAction) {
                        case PLUS -> existing.setCount(count + 1);
                        case MINUS -> {
                            if (count > 0) existing.setCount(count - 1);
                            else existing.setCount(0);
                        }
                        case DELETE -> existing.setCount(0);
                        default -> {
                            return Mono.error(new IllegalStateException("Unexpected value: " + cartItemAction));
                        }
                    }

                    return itemRepository.save(existing);
                })
                .then();
    }

    private Mono<Item> getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new NotFoundException("Item doesn't exist with id: %s".formatted(itemId))));
    }
}
