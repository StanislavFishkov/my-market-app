package ru.yandex.practicum.mymarket.dto.item;

public record ItemSearchRequestDto(String search, ItemSort sort, Integer pageNumber, Integer pageSize) {
    public ItemSearchRequestDto {
        if (search != null && search.isBlank()) search = null;
        if (sort == null) sort = ItemSort.NO;
        if (pageNumber == null || pageNumber < 1) pageNumber = 1;
            else pageNumber = pageNumber - 1;
        if (pageSize == null || pageSize < 1) pageSize = 5;
    }
}