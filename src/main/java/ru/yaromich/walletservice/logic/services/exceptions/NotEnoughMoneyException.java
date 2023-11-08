package ru.yaromich.walletservice.logic.services.exceptions;

public class NotEnoughMoneyException extends TransactionException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
