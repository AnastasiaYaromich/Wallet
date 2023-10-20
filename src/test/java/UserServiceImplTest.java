
import domain.models.User;
import logic.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import repositories.repositories.UserRepository;
import services.exceptions.MainException;
import services.exceptions.user.UserAlreadyExistException;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
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
    public void findUserByLogin() throws MainException {
        String login = "test";
        String password = "999";
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        when(userRepository.findUserByLogin(login)).thenReturn(user);
        assertEquals(user, userService.findUserByLogin(login));
    }

    @Test
    public void users() {
        List<User> userList = userRepository.users();
        assertEquals(0, userList.size());
    }

}
