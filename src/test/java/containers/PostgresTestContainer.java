package containers;

import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    public static final String IMAGE_VERSION = "postgres";
    public static final String DATABASE_NAME = "test";
    public static PostgreSQLContainer container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    @ClassRule
    public static PostgreSQLContainer getInstance() {
        if(container == null) {
            container = new PostgresTestContainer().withDatabaseName(DATABASE_NAME)
                    .withUsername("testUser")
                    .withPassword("1");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
