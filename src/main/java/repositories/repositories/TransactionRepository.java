package repositories.repositories;

import domain.models.Transaction;
import services.exceptions.MainException;

import java.util.List;

public interface TransactionRepository {
    boolean isTransactionIdUnique(String login, String id);
    boolean withdraw(String login, double amount) throws MainException;
    void replenish(String login, double amount) throws MainException;
    Double currentBalance(String login);
    void saveTransaction(String login, String id, String type, String condition, String note);
    List<Transaction> receiveTransactionsHistory(String login);
}
