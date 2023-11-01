package com.yaromich.wallet.services;

import com.yaromich.wallet.domain.dto.TransactionDto;
import com.yaromich.wallet.domain.dto.TransactionRequest;
import com.yaromich.wallet.domain.dto.UserDto;
import com.yaromich.wallet.logic.exceptions.TransactionException;
import com.yaromich.wallet.logic.exceptions.UserException;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    boolean isTransactionIdUnique(UserDto userDto, String transactionId) throws TransactionException, UserException;
    void withdraw(TransactionRequest transactionRequest) throws TransactionException, UserException;
    void replenish(TransactionRequest transactionRequest) throws TransactionException, UserException;
    Map<String, List<TransactionDto>> findAllByUserLogin(String login) throws TransactionException, UserException;
}
