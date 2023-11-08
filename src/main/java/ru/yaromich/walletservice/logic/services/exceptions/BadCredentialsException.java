package ru.yaromich.walletservice.logic.services.exceptions;

public class BadCredentialsException extends UserException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
