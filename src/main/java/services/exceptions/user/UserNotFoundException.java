package services.exceptions.user;

import services.exceptions.MainException;

/**
 * UserNotFoundException throws if user does not found in storage.
 */
public class UserNotFoundException extends MainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
