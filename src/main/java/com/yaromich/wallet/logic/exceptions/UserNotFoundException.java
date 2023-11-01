package com.yaromich.wallet.logic.exceptions;

/**
 * UserNotFoundException throws if user does not found in storage.
 */
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
