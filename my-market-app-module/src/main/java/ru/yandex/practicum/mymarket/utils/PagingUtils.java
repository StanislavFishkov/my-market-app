package ru.yandex.practicum.mymarket.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.springframework.data.relational.core.query.Query.query;

public final class PagingUtils {

    private PagingUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <E, D> Mono<Page<D>> selectPage(R2dbcEntityTemplate template, Criteria criteria, Pageable pageable,
                                                  Class<E> clz, Function<E, D> mapper) {
        Mono<List<D>> list = template.select(clz)
                .matching(query(criteria).with(pageable))
                .all()
                .map(mapper)
                .collectList();
        Mono<Long> count = template.select(clz)
                .matching(query(criteria))
                .count();
        return Mono.zip(list, count)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }
}