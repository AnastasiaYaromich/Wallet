package ru.yaromich.walletservice.logic.services.exceptions;

public class TransactionIdIsNotUniqueException extends TransactionException {
    public TransactionIdIsNotUniqueException(String message) {
        super(message);
    }
}
