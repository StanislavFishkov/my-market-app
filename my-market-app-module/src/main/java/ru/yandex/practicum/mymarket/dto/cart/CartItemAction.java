package ru.yandex.practicum.mymarket.dto.cart;

import java.util.Arrays;

public enum CartItemAction {
    PLUS,
    MINUS,
    DELETE;

    public static CartItemAction from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}