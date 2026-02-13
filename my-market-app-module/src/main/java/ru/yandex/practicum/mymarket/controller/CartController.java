package ru.yandex.practicum.mymarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.cart.CartItemActionDto;
import ru.yandex.practicum.mymarket.dto.cart.IdRequired;
import ru.yandex.practicum.mymarket.service.cart.CartService;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/items")
    public Mono<String> getCartItems(Model model) {
        log.info("GET /cart/items");

        return cartService.findCartItems()
                .collectList()
                .doOnNext(cartItems -> {
                    model.addAttribute("items", cartItems);
                    model.addAttribute("total", cartItems.stream()
                            .mapToLong(item -> item.getPrice() * item.getCount()).sum());
                })
                .thenReturn("cart");
    }

    @PostMapping("/items")
    public Mono<String> changeItems(@Validated(IdRequired.class) @ModelAttribute CartItemActionDto cartItemActionDto) {
        log.info("POST /cart/items with params(id={}, action={})", cartItemActionDto.id(), cartItemActionDto.action());

        return cartService.changeItemCount(cartItemActionDto.id(), cartItemActionDto.action())
                .thenReturn("redirect:" + UriComponentsBuilder
                        .fromPath("/cart/items")
                        .build()
                        .toUriString());
    }
}