package ru.yandex.practicum.mymarket.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.mymarket.dto.item.ItemWithCountDto;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    List<ItemWithCountDto> items;
    @Builder.Default
    Long balance = 0L;
    boolean paymentAvailable;

    public Long getTotalSum() {
        return items.stream().mapToLong(item -> item.getPrice() * item.getCount()).sum();
    }

    public boolean canCheckout() {
        return paymentAvailable && balance != null && balance >= getTotalSum();
    }
}