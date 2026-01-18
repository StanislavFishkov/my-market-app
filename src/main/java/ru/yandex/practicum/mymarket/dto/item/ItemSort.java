package ru.yandex.practicum.mymarket.dto.item;

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
}