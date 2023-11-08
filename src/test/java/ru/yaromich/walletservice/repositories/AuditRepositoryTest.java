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
import ru.yaromich.walletservice.domain.models.Audit;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.infrastructure.out.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class AuditRepositoryTest {

    private static AuditRepository auditRepository;
    private static UserRepository userRepository;
    private static TransactionRepository transactionRepository;
    private static UserRoleRepository userRoleRepository;
    private static RoleRepository roleRepository;
    private static User user;
    private static Audit audit;

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
            auditRepository = new AuditRepositoryImpl(dataSource, userRepository);
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

        audit = new Audit();
        audit.setUser(user);
        audit.setBalance(user.getBalance());
        audit.setStatus("SUCCESS");
        audit.setNote("");

        user.setAudits(List.of(audit));
    }

    @Test
    public void shouldSaveAudit() {
        auditRepository.save(user.getLogin(), audit);
        List<Audit> audits = auditRepository.findAllByUserName(user.getLogin());
        Assertions.assertEquals(1, audits.size());
    }

    @Test
    public void shouldFindAllByUserName() {
        List<Audit> audits = auditRepository.findAllByUserName(user.getLogin());
        Assertions.assertEquals(1, audits.size());
    }
}
