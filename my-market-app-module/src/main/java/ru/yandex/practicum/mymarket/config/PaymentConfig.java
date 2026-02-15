package ru.yandex.practicum.mymarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.mymarket.payment.api.BalanceApi;
import ru.yandex.practicum.mymarket.payment.api.PaymentApi;

@Configuration
public class PaymentConfig {
    @Bean
    public BalanceApi balanceApi(){
        return new BalanceApi();
    }

    @Bean
    public PaymentApi paymentApi(){
        return new PaymentApi();
    }
}
