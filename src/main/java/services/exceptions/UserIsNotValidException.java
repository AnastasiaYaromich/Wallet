package services.exceptions;

public class UserIsNotValidException extends ValidationException {
    public UserIsNotValidException(String message) {
        super(message);
    }
}
