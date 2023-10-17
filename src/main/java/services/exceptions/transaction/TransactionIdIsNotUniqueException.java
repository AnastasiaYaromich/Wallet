package services.exceptions.transaction;

import services.exceptions.MainException;

/**
 * TransactionIdIsNotUniqueException throws if transaction id isn't unique.
 */
public class TransactionIdIsNotUniqueException extends MainException {
    public TransactionIdIsNotUniqueException(String message) {
        super(message);
    }
}
