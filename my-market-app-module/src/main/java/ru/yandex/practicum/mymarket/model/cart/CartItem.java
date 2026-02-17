package ru.yandex.practicum.mymarket.model.cart;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cart_items")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    Long id;

    @Column("item_id")
    Long itemId;

    @Min(0)
    @Column
    @Builder.Default
    Integer count = 0;
}