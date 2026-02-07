package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class RootController {

    @GetMapping("/")
    public Mono<String> getRoot() {
        return Mono.just("redirect:/items");
    }
}
