package infrastructure.out.user;

import logic.UserServiceImpl;
import services.UserService;

import java.sql.SQLException;

public class UserServiceSingleton {
    private static UserService userServiceInstance;

    public static UserService getUserService() throws SQLException {
        if(userServiceInstance == null) {
            userServiceInstance = new UserServiceImpl(UserRepositorySingleton.getUserRepositoryInstance());
        }
        return userServiceInstance;
    }
}
