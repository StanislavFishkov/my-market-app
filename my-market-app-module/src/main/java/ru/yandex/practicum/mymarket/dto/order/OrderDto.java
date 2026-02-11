package ru.yandex.practicum.mymarket.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    Long id;
    List<OrderItemDto> items;

    public Long totalSum() {
        return items.stream().mapToLong(item -> item.price() * item.count()).sum();
    }
}