package ru.yandex.practicum.mymarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.view.Rendering;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    public Rendering handleNotFound(NotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public Rendering handleInsufficientFunds(InsufficientFundsException ex) {
        return handleException(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PaymentServiceUnavailableException.class)
    public Rendering handlePaymentServiceUnavailable(PaymentServiceUnavailableException ex) {
        return handleException(ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Rendering handleException(Exception ex, HttpStatus status) {
        return Rendering.view("error/error")
                .status(status)
                .modelAttribute("message", ex.getMessage())
                .modelAttribute("errorCode", status.value())
                .modelAttribute("errorName", status.toString())
                .build();
    }
}