package ru.yandex.practicum.mymarket.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    Long id;
    List<OrderItemDto> items;

    public Long getTotalSum() {
        return items.stream().mapToLong(item -> item.getPrice() * item.getCount()).sum();
    }
}