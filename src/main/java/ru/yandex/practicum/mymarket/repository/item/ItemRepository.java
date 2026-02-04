package ru.yandex.practicum.mymarket.repository.item;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.mymarket.model.item.Item;

public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {
    Flux<Item> findAllByCountGreaterThan(int count);
}