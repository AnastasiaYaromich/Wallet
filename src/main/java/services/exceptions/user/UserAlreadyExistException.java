package services.exceptions.user;

import services.exceptions.MainException;

/**
 * UserAlreadyExistException throws if user with these credentials already exist in storage.
 */
public class UserAlreadyExistException extends MainException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
