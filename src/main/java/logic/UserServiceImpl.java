package logic;

import domain.models.User;
import repositories.repositories.UserRepository;
import services.exceptions.MainException;
import services.exceptions.user.UserAlreadyExistException;
import services.exceptions.user.UserNotFoundException;
import services.services.UserService;

import java.util.List;

/**
 * The UserServiceImpl class implements UserService interface.
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * The addUser() method calls addUser() method in UserRepositoryI implementation.
     * @param user user to save.
     * @return method return the user that was saved in storage.
     * @throws MainException throws if user already exist.
     */
    @Override
    public User addUser(User user) throws MainException {
        if(userRepository.findUserByLogin(user.getLogin()) != null) {
            throw new UserAlreadyExistException("User already exist.");
        }
        return userRepository.addUser(user);
    }

    /**
     * The findUserByLogin() method calls findUserByLogin() method in UserRepositoryI implementation.
     * @param login user login.
     * @return method return the founded user.
     * @throws MainException throws if user does not found.
     */
    @Override
    public User findUserByLogin(String login) throws MainException {
        User user = userRepository.findUserByLogin(login);
        if(user == null) {
            throw new UserNotFoundException("User with this login does not exist.");
        }
        return user;
    }

    /**
     * The users() method calls users() method in UserRepositoryI implementation.
     * @return list of users.
     */
    @Override
    public List<User> users() {
        return userRepository.users();
    }
}
