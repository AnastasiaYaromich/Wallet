package services;

import dto.TransactionDto;
import services.exceptions.TransactionException;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    boolean isTransactionIdUnique(String login, String id) throws TransactionException;
    void withdraw(String login, BigDecimal amount) throws TransactionException;
    void replenish(String login, BigDecimal amount);
    BigDecimal findBalanceByLogin(String login);
    TransactionDto save(String login, TransactionDto transactionDto);
    List<TransactionDto> findAll(String login);
}
