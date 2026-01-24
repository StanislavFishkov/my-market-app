package ru.yandex.practicum.mymarket.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mymarket.config.PostgresSQLTestContainer;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.model.item.Item;
import ru.yandex.practicum.mymarket.repository.item.ItemRepository;
import ru.yandex.practicum.mymarket.service.item.ItemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest extends PostgresSQLTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ItemService itemService;

    @MockitoSpyBean
    private ItemRepository itemRepository;

    @Test
    @SneakyThrows
    @Transactional
    void shouldIncreaseCountInCart_whenActionPlusInvoked() {
        // Given
        Item item = Item.builder()
                .title("title")
                .description("description")
                .price(100L)
                .count(0)
                .build();

        item = itemRepository.save(item);
        Mockito.reset(itemRepository);

        // When + Then
        MvcResult result = mockMvc.perform(post("/cart/items")
                        .param("id", item.getId().toString())
                        .param("action", "PLUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"))
                .andReturn();

        // check item count
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getCount()).isEqualTo(1);

        // verify calls
        verify(itemService, only()).changeItemCount(item.getId(), CartItemAction.PLUS);

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(1)).save(captor.capture());

        Item savedItem = captor.getValue();
        assertThat(savedItem.getCount()).isEqualTo(1);
        assertThat(savedItem.getTitle()).isEqualTo("title");

        // check redirect url
        String redirectedUrl = result.getResponse().getRedirectedUrl();
        assertThat(redirectedUrl).isNotNull();

        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}