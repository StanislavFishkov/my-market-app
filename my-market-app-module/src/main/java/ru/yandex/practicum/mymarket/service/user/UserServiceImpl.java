package ru.yandex.practicum.mymarket.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.exception.NotFoundException;
import ru.yandex.practicum.mymarket.model.user.User;
import ru.yandex.practicum.mymarket.repository.user.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Mono<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .switchIfEmpty(Mono.error(new NotFoundException("User doesn't exist with login: %s".formatted(login))));
    }
}