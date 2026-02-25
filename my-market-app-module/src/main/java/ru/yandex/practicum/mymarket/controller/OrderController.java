package ru.yandex.practicum.mymarket.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.security.SecurityUser;
import ru.yandex.practicum.mymarket.service.order.OrderService;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public Mono<String> getOrderById(@PathVariable("id") @NotNull Long orderId,
                                     @RequestParam(required = false) Boolean newOrder,
                                     Model model) {
        log.info("GET /orders/{} with params(newOrder={}))", orderId,  newOrder);

        return orderService.getOrderById(orderId)
                .doOnNext(orderDto -> {
                    model.addAttribute("order", orderDto);
                    model.addAttribute("newOrder", newOrder);
                })
                .thenReturn("order");
    }

    @GetMapping("/orders")
    public Mono<String> findOrders(Model model, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("GET /orders ");

        return orderService.findOrders(securityUser.id())
                .collectList()
                .doOnNext(orders -> model.addAttribute("orders", orders))
                .thenReturn("orders");
    }

    @PostMapping("/buy")
    public Mono<String> buy(@AuthenticationPrincipal SecurityUser securityUser) {
        return orderService.createOrder(securityUser.id())
                .map("redirect:/orders/%s?newOrder=true"::formatted);
    }
}