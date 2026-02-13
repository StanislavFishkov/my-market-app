package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemPageDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCacheServiceImpl implements ItemCacheService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Cacheable(value = "item", key = "#itemId")
    public Mono<ItemDto> getItemById(Long itemId) {
        log.debug("Item not found in cache, requested from db: id={}", itemId);

        return getItemOrThrow(itemId)
                .map(itemMapper::toDto);
    }

    @Override
    @Cacheable(value = "item_pages", key = """
            #itemSearchRequestDto.sort + ":" + #itemSearchRequestDto.pageNumber + ":" + #itemSearchRequestDto.pageSize
            + ":" + T(java.util.Objects).toString(#itemSearchRequestDto.search, "")
            """, condition = "#itemSearchRequestDto.search == null || #itemSearchRequestDto.search.length() < 50")
    public Mono<ItemPageDto> findItems(ItemSearchRequestDto itemSearchRequestDto) {
        log.debug("Items not found in cache, requested from db: criteria={}", itemSearchRequestDto);

        return itemRepository.findItems(itemSearchRequestDto)
                .map(page -> page.map(itemMapper::toDto))
                .map(page -> ItemPageDto.builder()
                        .total(page.getTotalElements())
                        .content(page.getContent())
                        .build()
                );
    }

    private Mono<Item> getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new NotFoundException("Item doesn't exist with id: %s".formatted(itemId))));
    }
}