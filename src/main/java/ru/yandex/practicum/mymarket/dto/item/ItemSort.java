package ru.yandex.practicum.mymarket.dto.item;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public enum ItemSort {
    NO,
    ALPHA,
    PRICE;

    public static ItemSort from(String value) {
        if (value == null || value.isBlank()) {
            return NO;
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(NO);
    }

    public Sort toSpringSort() {
        return switch (this) {
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            case NO -> Sort.unsorted();
        };
    }
}