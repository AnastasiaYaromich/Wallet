import config.ContainersEnvironment;
import containers.PostgresTestContainer;
import domain.models.User;
import infrastructure.in.UserInMemory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.junit.jupiter.api.*;
import java.util.List;

public class UserInMemoryTest extends ContainersEnvironment {
    static PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();

    private UserInMemory userInMemory;


    @BeforeEach
    void setUp() {
        userInMemory = new UserInMemory();
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }


    @Test
    void findAll() {
        List<User> list = userInMemory.users();
        Assertions.assertEquals(5, list.size());
    }
}
