package ru.yandex.practicum.mymarket.repository.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.criteria.ItemCriteria;
import ru.yandex.practicum.mymarket.utils.PagingUtils;

import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<Page<Item>> findItems(ItemSearchRequestDto itemSearchRequestDto) {
        Criteria spec = ItemCriteria.searchInTitleOrDescription(itemSearchRequestDto.search());

        return PagingUtils.selectPage(r2dbcEntityTemplate, spec, itemSearchRequestDto.toPageable(), Item.class,
                Function.identity());
    }
}