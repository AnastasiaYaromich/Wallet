import domain.models.User;
import infrastructure.in.UserInMemory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.*;
import java.util.List;

@Testcontainers
public class UserInMemoryTest {

    private UserInMemory userInMemory;
    private static Connection connection;
    private User user;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void setConnection() throws SQLException {
        postgreSQLContainer.start();
        connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        ReceiveDDatabaseData.init(connection);
    }

    @AfterAll
    public static void stop() throws SQLException {
        connection.close();
        postgreSQLContainer.stop();
    }

    @BeforeEach
    public void setDatabaseState() {
        userInMemory = new UserInMemory();
        UserInMemory.connection = connection;
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.users WHERE login=?")) {
            statement.setString(1, "someUser");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void users() {
        List<User> list = userInMemory.users();
        Assertions.assertEquals(0, list.size());
        user = createTestUser();
        userInMemory.addUser(user);
        list = userInMemory.users();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void addUser() {
        user = createTestUser();
        User addedUser = userInMemory.addUser(user);
        Assertions.assertEquals(1, userInMemory.users().size());
        Assertions.assertEquals(userInMemory.findUserByLogin(addedUser.getLogin()).getLogin(), user.getLogin());
    }

    @Test
    public void findUserByLogin() {
        user = createTestUser();
        userInMemory.addUser(user);
        Assertions.assertEquals(user.getLogin(), userInMemory.findUserByLogin(user.getLogin()).getLogin());
        Assertions.assertNotNull(userInMemory.findUserByLogin(user.getLogin()));
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(0.0);
        user.setRole("user");
        return user;
    }
}
