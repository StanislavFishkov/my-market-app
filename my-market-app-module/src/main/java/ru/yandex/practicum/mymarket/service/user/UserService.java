package ru.yandex.practicum.mymarket.service.user;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.user.User;

public interface UserService {
    Mono<User> getUserByLogin(String login);
}
