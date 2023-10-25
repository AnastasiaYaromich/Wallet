import domain.models.User;
import infrastructure.out.user.UserRepositoryImpl;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Testcontainers
public class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;
    private static Connection connection;
    private User user;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void setConnection() throws SQLException {
        postgreSQLContainer.start();
        connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        DatabaseData.init(connection);
    }

    @AfterAll
    public static void stop() throws SQLException {
        connection.close();
        postgreSQLContainer.stop();
    }

    @BeforeEach
    public void setDatabaseState() {
        userRepository = new UserRepositoryImpl();
        UserRepositoryImpl.connection = connection;
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.users WHERE login=?")) {
            statement.setString(1, "someUser");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findAll() {
        List<User> list = userRepository.findAll();
        Assertions.assertEquals(0, list.size());
        user = createTestUser();
        userRepository.save(user);
        list = userRepository.findAll();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void save() {
        user = createTestUser();
        User addedUser = userRepository.save(user);
        Assertions.assertEquals(1, userRepository.findAll().size());
        Assertions.assertEquals(userRepository.findUserByLogin(addedUser.getLogin()).get().getLogin(), user.getLogin());
    }

    @Test
    public void findUserByLogin() {
        user = createTestUser();
        userRepository.save(user);
        Assertions.assertEquals(user.getLogin(), userRepository.findUserByLogin(user.getLogin()).get().getLogin());
        Assertions.assertNotNull(userRepository.findUserByLogin(user.getLogin()));
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRole("user");
        return user;
    }
}
