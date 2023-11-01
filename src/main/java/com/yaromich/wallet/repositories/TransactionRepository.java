package com.yaromich.wallet.repositories;

import com.yaromich.wallet.domain.model.Transaction;
import com.yaromich.wallet.domain.model.User;

import java.util.List;

public interface TransactionRepository {
    boolean isTransactionIdUnique(User user, String transactionId);
    Transaction save(Transaction transaction);
    List<Transaction> findAllByUserLogin(String login);
}
