
import domain.models.Transaction;
import domain.models.User;
import infrastructure.in.TransactionInMemory;
import infrastructure.in.UserInMemory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import services.exceptions.MainException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class TransactionInMemoryTest {

    private TransactionInMemory transactionInMemory;
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
        transactionInMemory = new TransactionInMemory();
        TransactionInMemory.connection = connection;
        UserInMemory userInMemory = new UserInMemory();
        UserInMemory.connection = connection;

        user = createTestUser();
        userInMemory.addUser(user);

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
    public void saveTransaction() {
        Assertions.assertEquals(0, transactionInMemory.receiveTransactionsHistory(user.getLogin()).size());

        Transaction transaction = createTransaction();

        transactionInMemory.saveTransaction(user.getLogin(), transaction.getTransactionId(),
                transaction.getType(), transaction.getCondition(), transaction.getNote());

        Assertions.assertEquals(1, transactionInMemory.receiveTransactionsHistory(user.getLogin()).size());
    }

    @Test
    public void isTransactionIdUnique() {
        Transaction transaction = createTransaction();
        transactionInMemory.saveTransaction(user.getLogin(), transaction.getTransactionId(),
                transaction.getType(), transaction.getCondition(), transaction.getNote());

        Assertions.assertTrue(transactionInMemory.isTransactionIdUnique(user.getLogin(), "2"));
        Assertions.assertFalse(transactionInMemory.isTransactionIdUnique(user.getLogin(), "1"));
    }

    @Test
    public void receiveTransactionsHistory() {
        List<Transaction> transactions = transactionInMemory.receiveTransactionsHistory(user.getLogin());

        Assertions.assertEquals(0, transactions.size());

        Transaction transaction = createTransaction();
        transactionInMemory.saveTransaction(user.getLogin(), transaction.getTransactionId(),
                transaction.getType(), transaction.getCondition(), transaction.getNote());

        Assertions.assertEquals(1, transactionInMemory.receiveTransactionsHistory(user.getLogin()).size());

        transactionInMemory.saveTransaction(user.getLogin(), transaction.getTransactionId(),
                transaction.getType(), transaction.getCondition(), transaction.getNote());
        Assertions.assertEquals(1, transactionInMemory.receiveTransactionsHistory(user.getLogin()).size());
    }


    @Test
    public void withdrawWithNotEnoughMoneyException() throws MainException {
        double amount = 150.0;
        MainException e = Assertions.assertThrows(MainException.class, () -> {transactionInMemory.withdraw(user.getLogin(), amount);}, "Not enough money in your wallet");
        Assertions.assertEquals("Not enough money in your wallet", e.getMessage());
    }

    @Test
    public void withdrawSuccessfullyCase() throws MainException {
        double amount = 100.0;
        Assertions.assertTrue(transactionInMemory.withdraw(user.getLogin(), amount));
    }

    @Test
    public void replenish() {
        double amount = 1000.0;
        transactionInMemory.replenish(user.getLogin(), amount);
        Assertions.assertEquals(1100,user.getBalance() + amount);
    }

    @Test
    public void currentBalance() {
        Assertions.assertEquals(100.0, transactionInMemory.currentBalance(user.getLogin()));
    }


    private User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(100.0);
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
