package ru.yandex.practicum.mymarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    public Mono<String> handleNotFound(NotFoundException ex, Model model, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        model.addAttribute("message", ex.getMessage());
        return Mono.just("error/404");
    }
}