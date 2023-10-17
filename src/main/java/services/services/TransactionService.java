package services.services;

import domain.models.Transaction;
import services.exceptions.MainException;
import java.util.List;

/**
 * The TransactionService interface
 */
public interface TransactionService {
    boolean isTransactionIdUnique(String login, String id) throws MainException;
    boolean withdraw(String login, double amount) throws MainException;
    void replenish(String login, double amount) throws MainException;
    Double currentBalance(String login);
    void saveTransaction(String login, String id, String type, String condition, String note) throws MainException;
    List<Transaction> receiveTransactionsHistory(String login);
}
