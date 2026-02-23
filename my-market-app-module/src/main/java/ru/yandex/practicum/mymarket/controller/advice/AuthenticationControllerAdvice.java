package ru.yandex.practicum.mymarket.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class AuthenticationControllerAdvice {
    @ModelAttribute("authentication")
    public Mono<Authentication> addAuthAttribute(@AuthenticationPrincipal Mono<Authentication> authMono) {
        return authMono;
    }
}