package ru.yandex.practicum.mymarket.service.item;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(Long itemId);

    Page<ItemDto> findItems(ItemSearchRequestDto itemSearchRequestDto);

    List<ItemDto> findCartItems();

    void changeItemCount(Long itemId, CartItemAction cartItemAction);
}