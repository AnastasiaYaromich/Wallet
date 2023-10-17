package services.services;

import domain.models.User;
import services.exceptions.MainException;
import java.util.List;

/**
 * The UserService interface
 */
public interface UserService {
    User findUserByLogin(String login) throws MainException;
    User addUser(User user)  throws MainException;
    List<User> users() throws MainException;
}
