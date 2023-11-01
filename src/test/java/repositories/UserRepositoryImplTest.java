package repositories;

import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.infrastructure.out.RoleRepositoryImpl;
import com.yaromich.wallet.infrastructure.out.UserRepositoryImpl;
import com.yaromich.wallet.infrastructure.out.UserRoleRepositoryImpl;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Testcontainers
public class UserRepositoryImplTest {

    private static UserRepositoryImpl userRepository;
    private static RoleRepositoryImpl roleRepository;
    private static UserRoleRepositoryImpl userRoleRepository;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:12.9-alpine");

    @BeforeAll
    public static void setUpConnection() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());

        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();

            userRepository = new UserRepositoryImpl(dataSource, roleRepository, userRoleRepository);
            roleRepository = new RoleRepositoryImpl(dataSource);
            userRoleRepository = new UserRoleRepositoryImpl(dataSource, roleRepository);
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByLogin() {
        String login = "The cat with the guitar";
        Optional<User> user = userRepository.findByLogin(login);
        AssertionsForClassTypes.assertThat(user).isNotEmpty();
    }

    @Test
    public void save() {
        User user = new User();
        user.setLogin("The cat with the guitar");
        user.setPassword("123");
        user.setBalance(BigDecimal.valueOf(0));

        userRepository.save(user);

        Optional<User> findedUser = userRepository.findByLogin(user.getLogin());
        Assertions.assertThat(user.getLogin()).isEqualTo(findedUser.get().getLogin());
    }






}
