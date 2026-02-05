package ru.yandex.practicum.mymarket.repository.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.model.item.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@ImportTestcontainers(PostgresSQLTestContainer.class)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSaveAndFind() {
        Item item = new Item();
        item.setTitle("Laptop");
        item.setPrice(1000L);
        item.setCount(5);

        Mono<Item> testMono =
                itemRepository.save(item)
                        .flatMap(saved -> {
                            assertThat(saved.getId()).isNotNull();
                            return itemRepository.findById(saved.getId());
                        });

        StepVerifier.create(testMono)
                .assertNext(found ->
                    assertThat(found)
                            .extracting(Item::getTitle, Item::getPrice, Item::getCount)
                            .containsExactly("Laptop", 1000L, 5)
                )
                .verifyComplete();
    }

    @Test
    void testFindAllByCountGreaterThan() {
        Item item1 = new Item();
        item1.setTitle("Laptop");
        item1.setPrice(1000L);
        item1.setCount(5);

        Item item2 = new Item();
        item2.setTitle("Mouse");
        item2.setPrice(50L);
        item2.setCount(2);

        Flux<Item> items = itemRepository.saveAll(List.of(item1, item2))
                .thenMany(itemRepository.findAllByCountGreaterThan(3));

        StepVerifier.create(items)
                .assertNext(found ->
                    assertThat(found)
                            .extracting(Item::getTitle, Item::getPrice, Item::getCount)
                            .containsExactly("Laptop", 1000L, 5)
                )
                .verifyComplete();
    }
}