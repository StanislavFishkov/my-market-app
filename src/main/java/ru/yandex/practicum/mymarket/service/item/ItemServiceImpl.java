package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.ItemMapper;
import ru.yandex.practicum.mymarket.model.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.repository.item.specification.ItemSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto getItemById(Long itemId) {
        log.debug("Item requested: id={}", itemId);

        Item item = getItemOrThrow(itemId);
        return itemMapper.toDto(item);
    }

    @Override
    public Page<ItemDto> findItems(ItemSearchRequestDto itemSearchRequestDto) {
        log.debug("Items requested: criteria={}", itemSearchRequestDto);

        Specification<Item> spec = ItemSpecifications.searchInTitleOrDescription(itemSearchRequestDto.search());

        Page<Item> page = itemRepository.findAll(spec, itemSearchRequestDto.toPageable());

        return page.map(itemMapper::toDto);
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item doesn't exist with id: %s".formatted(itemId)));
    }
}
