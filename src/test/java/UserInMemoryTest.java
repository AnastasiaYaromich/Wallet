import domain.models.User;
import infrastructure.in.UserInMemory;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

@Testcontainers
public class UserInMemoryTest {

    private static UserInMemory userInMemory;
    private static Connection connection;
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void setConnection() throws SQLException, LiquibaseException {
        connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        userInMemory = new UserInMemory();
        UserInMemory.connection = connection;
        ReceiveDDatabaseData.init(connection);
    }

    @Test
    public void findUserByLogin() {
        User user = new User();
        String login = "Afina";
        user.setLogin(login);

        userInMemory.addUser(user);
        User addedUser = userInMemory.findUserByLogin(user.getLogin());
        assertEquals(user.getLogin(), addedUser.getLogin());
        assertEquals(null, userInMemory.findUserByLogin("cat"));
    }

    @Test
    public void addUser() {
        User user = new User();
        String login = "Test";
        user.setLogin(login);

        userInMemory.addUser(user);
        assertEquals(userInMemory.findUserByLogin(login).getLogin(), user.getLogin());
    }

    @Test
    public void users() {
        List<User> list = userInMemory.users();
        assertEquals(0, list.size());

        User user = new User();
        String login = "Tes1";
        user.setLogin(login);
        userInMemory.addUser(user);

        list = userInMemory.users();
        assertEquals(1, list.size());
    }











}
