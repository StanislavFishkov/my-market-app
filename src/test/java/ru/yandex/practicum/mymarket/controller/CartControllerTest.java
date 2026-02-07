package ru.yandex.practicum.mymarket.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.service.item.ItemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureWebTestClient
class CartControllerTest extends PostgresSQLTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoSpyBean
    private ItemService itemService;

    @MockitoSpyBean
    private ItemRepository itemRepository;

    @Test
    @SneakyThrows
    void shouldIncreaseCountInCart_whenActionPlusInvoked() {
        // Given
        Item item = Item.builder()
                .title("title")
                .description("description")
                .price(100L)
                .count(0)
                .build();
        String expectedRedirectUrl = "/cart/items";

        item = itemRepository.save(item).block();
        Mockito.reset(itemRepository);

        assertThat(item).isNotNull();
        Long itemId = item.getId();

        // When + Then
        webTestClient.post()
                .uri(uriBuilder ->  uriBuilder
                        .path("/cart/items")
                        .queryParam("id", itemId.toString())
                        .queryParam("action", "PLUS")
                        .build())
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", expectedRedirectUrl);

        // check item count
        Item updatedItem = itemRepository.findById(item.getId()).block();
        assertThat(updatedItem)
                .isNotNull()
                .extracting(Item::getCount)
                .isEqualTo(1);

        // verify calls
        verify(itemService, only()).changeItemCount(item.getId(), CartItemAction.PLUS);

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(1)).save(captor.capture());

        Item savedItem = captor.getValue();
        assertThat(savedItem.getCount()).isEqualTo(1);
        assertThat(savedItem.getTitle()).isEqualTo("title");

        // check redirect url
        webTestClient.get()
                .uri(expectedRedirectUrl)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html");
    }
}