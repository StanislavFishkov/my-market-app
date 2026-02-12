package ru.yandex.practicum.mymarket.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
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
