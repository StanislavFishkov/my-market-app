package ru.yandex.practicum.mymarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.mymarket.payment.ApiClient;
import ru.yandex.practicum.mymarket.payment.api.BalanceApi;
import ru.yandex.practicum.mymarket.payment.api.PaymentApi;

@Configuration
public class PaymentConfig {
    @Bean
    public ApiClient apiClient(@Value("${payment.server.url}") String baseUrl){
        return new ApiClient().setBasePath(baseUrl);
    }

    @Bean
    public BalanceApi balanceApi(ApiClient apiClient){
        return new BalanceApi(apiClient);
    }

    @Bean
    public PaymentApi paymentApi(ApiClient apiClient){
        return new PaymentApi(apiClient);
    }
}
