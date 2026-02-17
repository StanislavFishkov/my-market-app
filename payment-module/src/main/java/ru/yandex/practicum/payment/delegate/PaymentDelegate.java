package ru.yandex.practicum.payment.delegate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payment.api.PaymentsApiDelegate;
import ru.yandex.practicum.payment.config.PaymentProperties;
import ru.yandex.practicum.payment.dto.PaymentRequest;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentProperties.class)
public class PaymentDelegate implements PaymentsApiDelegate {
    private final PaymentProperties paymentProperties;

    @Override
    public Mono<ResponseEntity<Void>> makePayment(Mono<PaymentRequest> paymentRequest, ServerWebExchange exchange) {
        return paymentRequest.flatMap(request -> {
            if (request.getAmount() <= paymentProperties.balance())
                return Mono.just(ResponseEntity.ok().build());
            else
                return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build());
        });
    }
}
