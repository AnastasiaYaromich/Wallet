package ru.yaromich.walletservice.repositories;

import org.springframework.stereotype.Repository;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;

import java.util.List;

@Repository
public interface TransactionRepository {
    boolean isTransactionIdUnique(User user, String transactionId);
    Transaction save(Transaction transaction);
    List<Transaction> findAllByUser(User user);
}
