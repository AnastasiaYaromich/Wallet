package com.yaromich.wallet.repositories;

import com.yaromich.wallet.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);
    User save(User user);
    List<User> findAll();
    void update(User user);
}
