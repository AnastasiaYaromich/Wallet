package infrastructure.out;

import aop.annotations.Speed;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Speed
public class LiquibaseConnection {
    private static Connection connection;

    static {
        connect();
    }

    public static void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            ResourceBundle resourceBundle = ResourceBundle.getBundle("liquibase");
            String url = resourceBundle.getString("url");
            String username = resourceBundle.getString("username");
            String password = resourceBundle.getString("password");
            String changeLogFile = resourceBundle.getString("changeLogFile");
            connection = DriverManager.getConnection(url, username, password);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(resourceBundle.getString("liquibaseSchemaName"));
            database.setDefaultSchemaName("defaultSchemaName");

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migrations was successful!");
        } catch (ClassNotFoundException | SQLException | LiquibaseException e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible to connect to DB");
        }
    }

    public static Connection getConnection() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("liquibase");
        String url = resourceBundle.getString("url");
        String username = resourceBundle.getString("username");
        String password = resourceBundle.getString("password");
        return DriverManager.getConnection(url, username, password);
    }

    public static void disconnect() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
