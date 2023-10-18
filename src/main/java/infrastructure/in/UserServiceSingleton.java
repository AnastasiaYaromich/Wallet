package infrastructure.in;

import logic.UserServiceImpl;
import services.services.UserService;

public class UserServiceSingleton {
    private static UserService userServiceInstance;

    /**
     * @return UserService implementation.
     */
    public static UserService getUserService() {
        if(userServiceInstance == null) {
            userServiceInstance = new UserServiceImpl(UserRepositorySingleton.getUserRepositoryInstance());
        }
        return userServiceInstance;
    }
}
