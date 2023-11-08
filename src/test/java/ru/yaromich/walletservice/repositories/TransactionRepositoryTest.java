package ru.yaromich.walletservice.repositories;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.infrastructure.out.TransactionRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class TransactionRepositoryTest {
    private static TransactionRepository transactionRepository;
    private static Transaction transaction;
    private static User user;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @BeforeAll
    static void setConnection() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUser(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());

        try(Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection)
            );
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();
            transactionRepository = new TransactionRepositoryImpl(dataSource);
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setData() {
        user = new User();
        user.setId(1L);
        user.setLogin("Anastasia");
        user.setPassword("12345");

        transaction = new Transaction();
        transaction.setTransactionId("1");
        transaction.setType("replenish");
        transaction.setCondition("SUCCESS");
        transaction.setNote("");
        transaction.setUser(user);

        user.setTransactions(List.of(transaction));
    }

    @Test
    public void shouldReturnTrueIsTransactionUnique() {
        Assertions.assertTrue(transactionRepository.isTransactionIdUnique(user, transaction.getTransactionId()));
    }

    @Test
    public void shouldSaveTransaction() {
        transactionRepository.save(transaction);
        List<Transaction> transactions = transactionRepository.findAllByUser(user);
        Assertions.assertEquals(1, transactions.size());
    }
}
