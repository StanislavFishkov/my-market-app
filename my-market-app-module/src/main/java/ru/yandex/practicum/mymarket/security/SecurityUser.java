package ru.yandex.practicum.mymarket.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.yandex.practicum.mymarket.model.user.User;

import java.util.Collection;
import java.util.List;

public record SecurityUser(Long id, String login, String password, String name) implements UserDetails {
    public SecurityUser(User user) {
        this(user.getId(), user.getLogin(), user.getPassword(), user.getName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}