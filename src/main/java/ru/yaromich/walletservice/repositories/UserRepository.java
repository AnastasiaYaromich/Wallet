package ru.yaromich.walletservice.repositories;

import org.springframework.stereotype.Repository;
import ru.yaromich.walletservice.domain.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByLogin(String login);
    User save(User user);
    List<User> findAll();
    void update(User user);
}
