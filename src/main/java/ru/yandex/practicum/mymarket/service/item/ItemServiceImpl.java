package ru.yandex.practicum.mymarket.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ItemServiceImpl implements ItemService {
    @Override
    public ItemDto getItemById(Long itemId) {
        return null;
    }

    @Override
    public Page<ItemDto> findItems(ItemSearchRequestDto itemSearchRequestDto) {
        return Page.empty();
    }
}
