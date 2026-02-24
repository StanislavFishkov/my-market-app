package ru.yandex.practicum.mymarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import ru.yandex.practicum.mymarket.security.SecurityUser;
import ru.yandex.practicum.mymarket.service.cart.CartService;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/items")
    public Mono<String> getCartItems(Model model, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("GET /cart/items");

        return cartService.getCart(securityUser.id())
                .doOnNext(cart -> {
                    model.addAttribute("items", cart.getItems());
                    model.addAttribute("total", cart.getTotalSum());
                    model.addAttribute("paymentUnavailable", !cart.isPaymentAvailable());
                    model.addAttribute("canCheckout", cart.canCheckout());
                })
                .thenReturn("cart");
    }

    @PostMapping("/items")
    public Mono<String> changeItems(@Validated(IdRequired.class) @ModelAttribute CartItemActionDto cartItemActionDto,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("POST /cart/items with params(id={}, action={})", cartItemActionDto.id(), cartItemActionDto.action());

        return cartService.changeItemCount(securityUser.id(), cartItemActionDto.id(), cartItemActionDto.action())
                .thenReturn("redirect:" + UriComponentsBuilder
                        .fromPath("/cart/items")
                        .build()
                        .toUriString());
    }
}