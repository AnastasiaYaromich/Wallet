package ru.yaromich.walletservice.logic.services.exceptions;

public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
