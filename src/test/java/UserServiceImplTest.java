import domain.models.User;
import logic.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repositories.repositories.UserRepository;
import services.exceptions.MainException;
import services.exceptions.user.UserAlreadyExistException;
import services.exceptions.user.UserNotFoundException;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void addUser() throws MainException {
        String login = "test";
        String password = "999";
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        when(userRepository.findUserByLogin(login)).thenReturn(user);
        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(user));
    }

    @Test
    public void findUserByLoginPositiveCase() throws MainException {
        String login = "test";
        String password = "999";
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        when(userRepository.findUserByLogin(anyString())).thenReturn(user);
        Assertions.assertEquals(user, userService.findUserByLogin(login));
    }

    @Test
    public void findUserByLoginNegativeCase() throws MainException {
        String login = "test";
        MainException e = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.findUserByLogin(login);}, "User with this login does not exist.");
        Assertions.assertEquals("User with this login does not exist.", e.getMessage());
    }

    @Test
    public void users() {
        List<User> userList = userRepository.users();
        Assertions.assertEquals(0, userList.size());

    }
}
