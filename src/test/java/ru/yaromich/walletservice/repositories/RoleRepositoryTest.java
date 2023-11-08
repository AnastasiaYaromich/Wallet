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
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.infrastructure.out.RoleRepositoryImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Testcontainers
public class RoleRepositoryTest {
    private static RoleRepository roleRepository;
    private static User user;
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
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setData() {
        user = new User();
        user.setLogin("Anastasia");
        user.setPassword("12345");
        user.setBalance(BigDecimal.ZERO);

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
    }

    @Test
    public void shouldFindByName() {
        Optional<Role> foundedRole = roleRepository.findByName(role.getName());
        Assertions.assertTrue(foundedRole.isPresent());
    }

    @Test
    public void shouldFindById() {
        Optional<Role> foundedRole = roleRepository.findById(role.getId());
        Assertions.assertTrue(foundedRole.isPresent());
    }
}
