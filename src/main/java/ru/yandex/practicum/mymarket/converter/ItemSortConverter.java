package ru.yandex.practicum.mymarket.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mymarket.dto.item.ItemSort;

@Component
public class ItemSortConverter implements Converter<String, ItemSort> {
    @Nullable
    @Override
    public ItemSort convert(@NonNull String source) {
        return ItemSort.from(source);
    }
}