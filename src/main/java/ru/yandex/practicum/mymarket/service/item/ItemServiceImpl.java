package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.mapper.item.ItemMapper;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.repository.item.specification.ItemSpecifications;

import java.util.List;

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

    @Override
    public List<ItemDto> findCartItems() {
        List<Item> cartItems = itemRepository.findAllByCountGreaterThan(0);

        return itemMapper.toDto(cartItems);
    }

    @Override
    @Transactional
    public void changeItemCount(Long itemId, CartItemAction cartItemAction) {
        Item item = getItemOrThrow(itemId);

        int count = item.getCount();

        switch (cartItemAction) {
            case PLUS -> item.setCount(count + 1);
            case MINUS -> {
                if (count > 0) item.setCount(count - 1);
                        else item.setCount(0);
            }
            case DELETE -> item.setCount(0);
            default -> throw new IllegalStateException("Unexpected value: " + cartItemAction);
        }

        itemRepository.save(item);
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item doesn't exist with id: %s".formatted(itemId)));
    }
}
