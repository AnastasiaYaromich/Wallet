package repositories;

import domain.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByLogin(String login);
    User save(User user);
    List<User> findAll();
}
