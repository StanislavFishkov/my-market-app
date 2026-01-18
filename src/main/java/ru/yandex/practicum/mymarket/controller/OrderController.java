package ru.yandex.practicum.mymarket.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.order.OrderDto;
import ru.yandex.practicum.mymarket.service.order.OrderService;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable("id") @NotNull Long orderId,
                               @RequestParam(required = false) Boolean newOrder,
                               Model model) {
        log.info("GET /orders/{} with params(newOrder={}))", orderId,  newOrder);

        OrderDto orderDto = orderService.getOrderById(orderId);

        model.addAttribute("order", orderDto);
        model.addAttribute("newOrder", newOrder);

        return "order";
    }

    @GetMapping("/orders")
    public String findOrders(Model model) {
        log.info("GET /orders ");

        List<OrderDto> orders = orderService.findOrders();

        model.addAttribute("orders", orders);

        return "orders";
    }

    @PostMapping("/buy")
    public String buy() {
        Long orderId = orderService.createOrder();

        return "redirect:/orders/%s?newOrder=true".formatted(orderId);
    }
}
