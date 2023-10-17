package repositories.repositories;

import domain.models.User;
import java.util.List;

/**
 * The UserRepositoryI interface
 */
public interface UserRepository {
    User findUserByLogin(String login);
    User addUser(User user);
    List<User> users();
}
