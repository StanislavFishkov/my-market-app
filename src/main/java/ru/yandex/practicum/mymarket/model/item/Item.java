package ru.yandex.practicum.mymarket.model.item;

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

@Table(name = "items")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    Long id;

    @NotBlank
    @Size(max = 255)
    @Column
    String title;

    @Size(max = 1000)
    @Column
    String description;

    @Size(max = 255)
    @Column
    String imgPath;

    @Min(0)
    @Column
    Long price;

    @Min(0)
    @Column
    Integer count;
}