package ru.yandex.practicum.mymarket.repository.item;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.model.item.Item;

public interface ItemCustomRepository {
    Mono<Page<Item>> findItems(ItemSearchRequestDto itemSearchRequestDto);
}
