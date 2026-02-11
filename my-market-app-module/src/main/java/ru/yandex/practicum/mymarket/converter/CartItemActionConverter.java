package ru.yandex.practicum.mymarket.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;

@Component
public class CartItemActionConverter implements Converter<String, CartItemAction> {
    @Nullable
    @Override
    public CartItemAction convert(@NonNull String source) {
        return CartItemAction.from(source);
    }
}