package ru.yandex.practicum.payment.delegate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payment.api.BalanceApiDelegate;
import ru.yandex.practicum.payment.config.PaymentProperties;
import ru.yandex.practicum.payment.dto.BalanceResponse;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentProperties.class)
public class BalanceDelegate implements BalanceApiDelegate {
    private final PaymentProperties paymentProperties;

    @Override
    @PreAuthorize("hasAuthority(T(ru.yandex.practicum.payment.config.SecurityConfig).PAYMENT_BALANCE_READ_AUTHORITY)")
    public Mono<ResponseEntity<BalanceResponse>> getBalance(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(BalanceResponse.builder().balance(paymentProperties.balance()).build()));
    }
}