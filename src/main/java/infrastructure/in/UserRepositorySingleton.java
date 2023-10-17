package infrastructure.in;

import repositories.repositories.UserRepository;

public class UserRepositorySingleton {
    private static UserRepository userRepositoryInstance;

    /**
     * @return UserRepositoryI implementation
     */
    public static UserRepository getUserRepositoryInstance() {
        if(userRepositoryInstance == null) {
            userRepositoryInstance = new UserInMemory();
        }
        return userRepositoryInstance;
    }
}
