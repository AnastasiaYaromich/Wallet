package com.yaromich.wallet.logic.exceptions;


/**
 * TransactionIdIsNotUniqueException throws if transaction id isn't unique.
 */
public class TransactionIdIsNotUniqueException extends TransactionException {
    public TransactionIdIsNotUniqueException(String message) {
        super(message);
    }
}