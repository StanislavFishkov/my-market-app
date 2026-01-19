package ru.yandex.practicum.mymarket.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Positive
    Long itemId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    String title;

    @Min(0)
    @Column(nullable = false)
    Long price;

    @Min(0)
    @Column(nullable = false)
    Integer count;
}