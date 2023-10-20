import domain.models.Audit;
import domain.models.User;
import infrastructure.in.AuditInMemory;
import infrastructure.in.UserInMemory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Testcontainers
public class AuditInMemoryTest {

    private AuditInMemory auditInMemory;
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
        auditInMemory = new AuditInMemory();
        AuditInMemory.connection = connection;
        UserInMemory userInMemory = new UserInMemory();
        UserInMemory.connection = connection;

        user = createTestUser();
        userInMemory.addUser(user);

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.audit WHERE user_id=?")) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void deleteUser() {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.users WHERE login=?")) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveAudit() {
        Assertions.assertEquals(0, auditInMemory.userActionAudit(user.getLogin()).size());
        Audit audit = createAudit();
        auditInMemory.saveAudit(user.getLogin(), audit);
        Assertions.assertEquals(1, auditInMemory.userActionAudit(user.getLogin()).size());
    }


    @Test
    public void userActionAudit() {
        Assertions.assertEquals(0, auditInMemory.userActionAudit(user.getLogin()).size());
        Audit audit = createAudit();
        auditInMemory.saveAudit(user.getLogin(), audit);
        Assertions.assertEquals(1, auditInMemory.userActionAudit(user.getLogin()).size());
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(0.0);
        user.setRole("user");
        return user;
    }

    private Audit createAudit() {
        Audit audit = new Audit();
        audit.setNote("");
        audit.setStatus("success");
        audit.setType("replenish");
        audit.setBalance(50.0);
        return audit;
    }
}
