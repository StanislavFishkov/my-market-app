package ru.yandex.practicum.mymarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.mymarket.dto.cart.CartItemAction;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.service.item.ItemService;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final ItemService itemService;

    @GetMapping("/items")
    public String getCartItems(Model model) {
        log.info("GET /cart/items");

        List<ItemDto> cartItems = itemService.findCartItems();

        model.addAttribute("items", cartItems);
        model.addAttribute("total", cartItems.stream()
                .mapToLong(item -> item.price() * item.count()).sum());

        return "cart";
    }

    @PostMapping("/items")
    public String changeItems(@RequestParam("id") Long itemId, @RequestParam("action") CartItemAction cartItemAction) {
        log.info("POST /cart/items with params(id={}, action={})", itemId, cartItemAction);

        itemService.changeItemCount(itemId, cartItemAction);

        return "redirect:" + UriComponentsBuilder
                .fromPath("/cart/items")
                .build()
                .toUriString();
    }
}