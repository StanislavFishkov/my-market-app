package ru.yandex.practicum.mymarket.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.mymarket.config.NoSecurityConfig;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.model.cart.CartItem;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.cart.CartItemRepository;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.service.cart.CartService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
@Import(NoSecurityConfig.class)
@AutoConfigureWebTestClient
class CartControllerTest extends PostgresSQLTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoSpyBean
    private CartService cartService;

    @MockitoSpyBean
    private ItemRepository itemRepository;

    @MockitoSpyBean
    private CartItemRepository cartItemRepository;

    @Test
    @SneakyThrows
    void shouldIncreaseCountInCart_whenActionPlusInvoked() {
        // Given
        Item item = Item.builder()
                .title("title")
                .description("description")
                .price(100L)
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
        CartItem cartItem = cartItemRepository.findByItemId(item.getId()).block();
        assertThat(cartItem)
                .isNotNull()
                .extracting(CartItem::getCount)
                .isEqualTo(1);

        // verify calls
        verify(cartService, only()).changeItemCount(item.getId(), CartItemAction.PLUS);
        verifyNoInteractions(itemRepository);

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository, times(1)).save(captor.capture());

        CartItem savedCartItem = captor.getValue();
        assertThat(savedCartItem.getCount()).isEqualTo(1);
        assertThat(savedCartItem.getItemId()).isEqualTo(item.getId());

        // check redirect url
        webTestClient.get()
                .uri(expectedRedirectUrl)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html");
    }
}