package repositories;

import domain.models.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository {
    boolean isTransactionIdUnique(String login, String id);
    boolean withdraw(String login, BigDecimal amount);
    void replenish(String login, BigDecimal amount);
    BigDecimal findBalanceByLogin(String login);
    Transaction save(String login, Transaction transaction);
    List<Transaction> findAll(String login);
}
