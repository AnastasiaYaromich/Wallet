package services.exceptions;

/**
 * UserAlreadyExistException throws if user with these credentials already exist in storage.
 */
public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
