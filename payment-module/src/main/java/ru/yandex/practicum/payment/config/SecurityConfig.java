package ru.yandex.practicum.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    public static final String PAYMENT_BALANCE_READ_AUTHORITY = "payment-balance-read";
    public static final String PAYMENT_BALANCE_WRITE_AUTHORITY = "payment-balance-write";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        return security
                .authorizeExchange(requests -> requests
                        .pathMatchers("/", "/actuator/**", "/swagger-ui*/**", "/v3/api-docs*/**").permitAll()
                        .pathMatchers("/balance").hasAuthority(PAYMENT_BALANCE_READ_AUTHORITY)
                        .pathMatchers("/payments").hasAuthority(PAYMENT_BALANCE_WRITE_AUTHORITY)
                        .anyExchange().denyAll()
                )
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(jwtSpec -> {
                            // Настройка ReactiveJwtAuthenticationConverter
                            ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
                            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                List<String> roles = jwt.getClaim("roles");

                                // Возвращаемый тип — Flux
                                return roles == null ? Flux.empty() : Flux.fromIterable(roles)
                                        .map(SimpleGrantedAuthority::new);
                            });

                            jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        })
                )
                .build();
    }
}