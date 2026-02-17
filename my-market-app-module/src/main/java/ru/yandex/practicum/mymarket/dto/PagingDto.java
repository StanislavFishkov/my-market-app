package ru.yandex.practicum.mymarket.dto;

import org.springframework.data.domain.Page;

public record PagingDto(int pageSize, int pageNumber, boolean hasPrevious, boolean hasNext) {

    public PagingDto(Page<?>  page) {
        this(page.getSize(),  page.getNumber() + 1, page.hasPrevious(), page.hasNext());
    }
}