package services.exceptions.transaction;

import services.exceptions.MainException;

/**
 * NotEnoughMoneyException throws if user don't have enough money to withdraw.
 */
public class NotEnoughMoneyException extends MainException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
