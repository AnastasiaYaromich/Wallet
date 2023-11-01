package com.yaromich.wallet.logic.exceptions;


/**
 * NotEnoughMoneyException throws if user don't have enough money to withdraw.
 */
public class NotEnoughMoneyException extends TransactionException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
