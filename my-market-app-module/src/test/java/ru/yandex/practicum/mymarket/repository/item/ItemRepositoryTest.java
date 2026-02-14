package ru.yandex.practicum.mymarket.repository.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.domain.Page;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.dto.item.ItemSearchRequestDto;
import ru.yandex.practicum.mymarket.dto.item.ItemSort;
import ru.yandex.practicum.mymarket.model.item.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@ImportTestcontainers(PostgresSQLTestContainer.class)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void cleanup() {
        databaseClient.sql("delete from items").then().block();
    }

    @Test
    void testSaveAndFind() {
        Item item = new Item();
        item.setTitle("Laptop");
        item.setPrice(1000L);

        Mono<Item> testMono =
                itemRepository.save(item)
                        .flatMap(saved -> {
                            assertThat(saved.getId()).isNotNull();
                            return itemRepository.findById(saved.getId());
                        });

        StepVerifier.create(testMono)
                .assertNext(found ->
                    assertThat(found)
                            .extracting(Item::getTitle, Item::getPrice)
                            .containsExactly("Laptop", 1000L)
                )
                .verifyComplete();
    }

    @Test
    void testFindItems() {
        Item item1 = new Item();
        item1.setTitle("Laptop");
        item1.setPrice(1000L);

        Item item2 = new Item();
        item2.setTitle("Mouse");
        item2.setPrice(50L);

        ItemSearchRequestDto itemSearchRequestDto = new ItemSearchRequestDto("TOP", ItemSort.NO, 1, 5);

        Mono<Page<Item>> items = itemRepository.saveAll(List.of(item1, item2))
                .then(itemRepository.findItems(itemSearchRequestDto));

        StepVerifier.create(items)
                .assertNext(page ->
                    assertThat(page.getContent())
                            .hasSize(1)
                            .first()
                            .extracting(Item::getTitle, Item::getPrice)
                            .containsExactly("Laptop", 1000L)
                )
                .verifyComplete();
    }
}