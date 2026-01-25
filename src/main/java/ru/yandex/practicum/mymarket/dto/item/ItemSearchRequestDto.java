package ru.yandex.practicum.mymarket.dto.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record ItemSearchRequestDto(String search, ItemSort sort, Integer pageNumber, Integer pageSize) {
    public ItemSearchRequestDto {
        if (search != null && search.isBlank()) {
            search = null;
        }
        sort = sort == null ? ItemSort.NO : sort;
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 5 : pageSize;
    }

    public Pageable toPageable() {
        return PageRequest.of(
                pageNumber - 1,
                pageSize,
                sort.toSpringSort()
        );
    }
}