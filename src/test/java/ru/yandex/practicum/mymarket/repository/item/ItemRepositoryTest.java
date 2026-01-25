package ru.yandex.practicum.mymarket.repository.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.model.item.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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

        Item saved = itemRepository.save(item);

        assertThat(saved.getId()).isNotNull();

        Item found = itemRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("Laptop");
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

        itemRepository.saveAll(List.of(item1, item2));

        List<Item> result = itemRepository.findAllByCountGreaterThan(3);

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Item::getTitle)
                .isEqualTo("Laptop");
    }
}