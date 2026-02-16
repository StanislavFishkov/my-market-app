package ru.yandex.practicum.mymarket.dto.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.apache.commons.codec.digest.DigestUtils;

public record ItemSearchRequestDto(String search, ItemSort sort, Integer pageNumber, Integer pageSize) {
    public ItemSearchRequestDto {
        if (search != null) {
            search = search.isBlank() ? null : search.trim().toLowerCase();
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

    public String cacheKey() {
        String searchPart = (search == null) ? "" :
                (search.length() <= 32 ? search : DigestUtils.md5Hex(search));
        return "%s:%d:%d:%s".formatted(sort, pageNumber, pageSize, searchPart);
    }
}