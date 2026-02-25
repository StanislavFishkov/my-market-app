package ru.yandex.practicum.mymarket.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.mymarket.payment.ApiClient;
import ru.yandex.practicum.mymarket.payment.api.BalanceApi;
import ru.yandex.practicum.mymarket.payment.api.PaymentApi;

@Configuration
public class PaymentConfig {
    @Bean
    public ApiClient apiClient(@Value("${payment.server.url}") String baseUrl,
                               @Qualifier("clientCredentialAuthorizedClientManager")
                               ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("my-market-app");

        WebClient webClient = ApiClient.buildWebClientBuilder()
                .filter(oauth)
//                .filter((request, next) -> {
//                    System.out.println("Request headers: " + request.headers());
//                    return next.exchange(request);
//                })
                .build();

        return new ApiClient(webClient).setBasePath(baseUrl);
    }

    @Bean
    public BalanceApi balanceApi(ApiClient apiClient) {
        return new BalanceApi(apiClient);
    }

    @Bean
    public PaymentApi paymentApi(ApiClient apiClient) {
        return new PaymentApi(apiClient);
    }
}
