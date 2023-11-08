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
import ru.yaromich.walletservice.infrastructure.out.UserRoleRepositoryImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class UserRoleRepositoryTest {

    private static RoleRepository roleRepository;
    private static UserRoleRepository userRoleRepository;
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
            userRoleRepository = new UserRoleRepositoryImpl(dataSource, roleRepository);
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
        user.setBalance(BigDecimal.ZERO);

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");
    }

    @Test
    public void shouldSaveRoleForUser() {
        userRoleRepository.save(user.getId(), role.getId());
        List<Role> roles = userRoleRepository.findAllByUserId(user.getId());
        Assertions.assertEquals(1, roles.size());
    }


    @Test
    public void shouldFindAllRolesForUser() {
        List<Role> roles = userRoleRepository.findAllByUserId(user.getId());
        Assertions.assertEquals(1, roles.size());
    }
}
