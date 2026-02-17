package ru.yandex.practicum.mymarket.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    Long id;
    String title;
    Long price;
    Integer count;
}