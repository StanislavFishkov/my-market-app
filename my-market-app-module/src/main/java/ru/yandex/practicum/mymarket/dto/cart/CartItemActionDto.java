package ru.yandex.practicum.mymarket.dto.cart;

import jakarta.validation.constraints.NotNull;

public record CartItemActionDto(@NotNull CartItemAction action, @NotNull(groups = IdRequired.class) Long id) {
}