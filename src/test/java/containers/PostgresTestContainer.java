package containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    public static final String IMAGE_VERSION = "postgres";
    public static final String DATABASE_NAME = "postgres";
    public static PostgreSQLContainer<PostgresTestContainer> container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainer<PostgresTestContainer> getInstance() {
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
