package ru.yandex.practicum.mymarket.model.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_items")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    Long id;

    @Column("order_id")
    Long orderId;

    @Column("item_id")
    Long itemId;

    @NotBlank
    @Size(max = 255)
    @Column
    String title;

    @Min(0)
    @Column
    Long price;

    @Min(0)
    @Column
    Integer count;
}