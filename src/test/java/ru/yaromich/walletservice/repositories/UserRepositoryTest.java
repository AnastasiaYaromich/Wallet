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
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.infrastructure.out.RoleRepositoryImpl;
import ru.yaromich.walletservice.infrastructure.out.TransactionRepositoryImpl;
import ru.yaromich.walletservice.infrastructure.out.UserRepositoryImpl;
import ru.yaromich.walletservice.infrastructure.out.UserRoleRepositoryImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Testcontainers
public class UserRepositoryTest {
    private static UserRepository userRepository;
    private static TransactionRepository transactionRepository;
    private static UserRoleRepository userRoleRepository;
    private static RoleRepository roleRepository;
    private static User user;
    private static Transaction transaction;
    private static Role role;

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
            roleRepository = new RoleRepositoryImpl(dataSource);
            transactionRepository = new TransactionRepositoryImpl(dataSource);
            userRoleRepository = new UserRoleRepositoryImpl(dataSource, roleRepository);
            userRepository = new UserRepositoryImpl(dataSource, transactionRepository, userRoleRepository);
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setData() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");

        user = new User();
        user.setLogin("Anastasia");
        user.setPassword("12345");
        user.setBalance(BigDecimal.ZERO);

        transaction = new Transaction();
        user.setTransactions(List.of());
        user.setRoles(List.of(role));
    }

    @Test
    public void shouldFindUserByLogin() {
        Optional<User> foundedUser = userRepository.findByLogin(user.getLogin());
        Assertions.assertTrue(foundedUser.isPresent());
    }

    @Test
    public void shouldNotFindUserByLogin() {
        Optional<User> foundedUser = userRepository.findByLogin("Cat with the guitar");
        Assertions.assertTrue(foundedUser.isEmpty());
    }

    @Test
    public void shouldFindAllUsers() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void shouldSaveUser() {
        User savedUser = new User();
        savedUser.setLogin("Cat with the guitar");
        savedUser.setPassword("BestCatInTheWorld");
        savedUser.setBalance(BigDecimal.ZERO);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void shouldUpdateUser() {
        User updatedUser = new User();
        updatedUser.setLogin("Cat with the guitar");
        updatedUser.setBalance(BigDecimal.valueOf(1000));
        userRepository.update(user);
    }
}
