import domain.models.Audit;
import domain.models.User;
import infrastructure.out.audit.AuditRepositoryImpl;
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

@Testcontainers
public class AuditRepositoryImlTest {
    private AuditRepositoryImpl auditRepository;
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
    public void setDatabaseState() throws SQLException {
        auditRepository = new AuditRepositoryImpl();
        AuditRepositoryImpl.connection = connection;
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        UserRepositoryImpl.connection = connection;

        user = createTestUser();
        userRepository.save(user);

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.audit WHERE user_id=?")) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void delete() {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.users WHERE login=?")) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() {
        Assertions.assertEquals(0, auditRepository.findAll(user.getLogin()).size());
        Audit audit = createAudit();
        auditRepository.save(user.getLogin(), audit);

        Assertions.assertEquals(1, auditRepository.findAll(user.getLogin()).size());
    }


    @Test
    public void findAll() {
        Assertions.assertEquals(0, auditRepository.findAll(user.getLogin()).size());
        Audit audit = createAudit();
        auditRepository.save(user.getLogin(), audit);
        Assertions.assertEquals(1, auditRepository.findAll(user.getLogin()).size());
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(BigDecimal.ZERO);
        user.setRole("user");
        return user;
    }

    private Audit createAudit() {
        Audit audit = new Audit();
        audit.setNote("");
        audit.setStatus("success");
        audit.setType("replenish");
        audit.setBalance(BigDecimal.valueOf(50.0));
        return audit;
    }
}
