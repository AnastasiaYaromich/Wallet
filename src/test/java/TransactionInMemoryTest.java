import domain.models.User;
import infrastructure.in.TransactionInMemory;
import infrastructure.in.UserInMemory;
import liquibase.exception.LiquibaseException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Testcontainers
public class TransactionInMemoryTest {

    private static TransactionInMemory transactionInMemory;
    private static Connection connection;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    public static void setConnection() throws SQLException, LiquibaseException {
        connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        transactionInMemory = new TransactionInMemory();
        TransactionInMemory.connection = connection;
        ReceiveDDatabaseData.init(connection);
    }


    @Test
    public void isTransactionIdUnique() {
        String login = "Afina";
        String transaction_id = "1";
        assertTrue(transactionInMemory.isTransactionIdUnique(login, transaction_id));
    }






}
