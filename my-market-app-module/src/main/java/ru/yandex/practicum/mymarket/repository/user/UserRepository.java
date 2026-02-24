package ru.yandex.practicum.mymarket.repository.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.user.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByLogin(String login);
}