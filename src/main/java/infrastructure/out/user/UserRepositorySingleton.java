package infrastructure.out.user;

import infrastructure.out.LiquibaseConnection;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRepositorySingleton {
    private static UserRepository userRepositoryInstance;

    public static UserRepository getUserRepositoryInstance() throws SQLException {
        if(userRepositoryInstance == null) {
            userRepositoryInstance = new UserRepositoryImpl();
        }
        return userRepositoryInstance;
    }
}
