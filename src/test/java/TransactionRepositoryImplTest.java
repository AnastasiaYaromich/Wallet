import domain.models.Transaction;
import domain.models.User;
import infrastructure.out.transaction.TransactionRepositoryImpl;
import infrastructure.out.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.*;
import services.exceptions.TransactionException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class TransactionRepositoryImplTest {

    private TransactionRepositoryImpl transactionRepository;
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
        transactionRepository = new TransactionRepositoryImpl();
        TransactionRepositoryImpl.connection = connection;
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        UserRepositoryImpl.connection = connection;

        user = createTestUser();
        userRepository.save(user);

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM wallet.transaction WHERE user_id=?")) {
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
    public void save() {
        Assertions.assertEquals(0, transactionRepository.findAll(user.getLogin()).size());
        Transaction transaction = createTransaction();
        transactionRepository.save(user.getLogin(), transaction);
        Assertions.assertEquals(1, transactionRepository.findAll(user.getLogin()).size());
    }

    @Test
    public void isTransactionIdUnique() {
        Transaction transaction = createTransaction();
        transactionRepository.save(user.getLogin(), transaction);
        Assertions.assertTrue(transactionRepository.isTransactionIdUnique(user.getLogin(), "2"));
        Assertions.assertFalse(transactionRepository.isTransactionIdUnique(user.getLogin(), "1"));
    }

    @Test
    public void findAll() {
        List<Transaction> transactions = transactionRepository.findAll(user.getLogin());
        Assertions.assertEquals(0, transactions.size());
        Transaction transaction = createTransaction();
        transactionRepository.save(user.getLogin(), transaction);
        Assertions.assertEquals(1, transactionRepository.findAll(user.getLogin()).size());
        transactionRepository.save(user.getLogin(), transaction);
        Assertions.assertEquals(2, transactionRepository.findAll(user.getLogin()).size());
    }


    @Test
    public void withdrawSuccessfullyCase() throws TransactionException {
        double amount = 100.0;
        Assertions.assertTrue(transactionRepository.withdraw(user.getLogin(), BigDecimal.valueOf(amount)));
    }

    @Test
    public void replenish() {
        double amount = 1000.0;
        Assertions.assertEquals(BigDecimal.valueOf(1100.0), BigDecimal.valueOf(amount).add(user.getBalance()));
    }

    @Test
    public void currentBalance() {
        Assertions.assertEquals(BigDecimal.valueOf(100.0), transactionRepository.findBalanceByLogin(user.getLogin()));
    }


    private User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(BigDecimal.valueOf(100.0));
        user.setRole("user");
        return user;
    }

    private Transaction createTransaction() {
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId("1");
        transaction1.setType("replenish");
        transaction1.setCondition("success");
        transaction1.setNote("");
        return transaction1;
    }
}
