package ru.yandex.practicum.mymarket.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    String title;
    String description;
    String imgPath;
    Long price;
    Integer count;
}
