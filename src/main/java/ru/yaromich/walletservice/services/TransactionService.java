package ru.yaromich.walletservice.services;

import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.dtos.TransactionRequest;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.logic.services.exceptions.TransactionException;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;

import java.util.List;

@Service
public interface TransactionService {
    boolean isTransactionIdUnique(UserDto userDto, String transactionId) throws TransactionException;
    void withdraw(TransactionRequest transactionRequest) throws TransactionException, UserException;
    void replenish(TransactionRequest transactionRequest) throws TransactionException, UserException;
    List<TransactionDto> findAllByUserLogin(String login) throws UserException;
}
