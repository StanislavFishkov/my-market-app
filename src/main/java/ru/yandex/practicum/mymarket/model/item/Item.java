package ru.yandex.practicum.mymarket.model.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    String title;

    @Size(max = 1000)
    @Column(length = 1000)
    String description;

    @Size(max = 255)
    @Column(length = 255)
    String imgPath;

    @Min(0)
    @Column(nullable = false)
    Long price;

    @Min(0)
    @Column(nullable = false)
    Integer count;
}