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
        HttpStatus status = HttpStatus.NOT_FOUND;
        return handleException(ex, model, exchange, status);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public Mono<String> handleInsufficientFunds(InsufficientFundsException ex, Model model, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        return handleException(ex, model, exchange, status);
    }

    @ExceptionHandler(PaymentServiceUnavailableException.class)
    public Mono<String> handlePaymentServiceUnavailable(PaymentServiceUnavailableException ex, Model model, ServerWebExchange exchange) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        return handleException(ex, model, exchange, status);
    }

    private Mono<String> handleException(Exception ex, Model model, ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("errorCode", status.value());
        model.addAttribute("errorName", status.toString());
        return Mono.just("error/error");
    }
}