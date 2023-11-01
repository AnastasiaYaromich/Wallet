package com.yaromich.wallet.logic.services;

import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.domain.dto.TransactionDto;
import com.yaromich.wallet.domain.dto.TransactionRequest;
import com.yaromich.wallet.domain.dto.UserDto;
import com.yaromich.wallet.domain.model.Transaction;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.infrastructure.out.TransactionRepositoryImpl;
import com.yaromich.wallet.logic.exceptions.*;
import com.yaromich.wallet.mappers.TransactionMapper;
import com.yaromich.wallet.mappers.UserMapper;
import com.yaromich.wallet.services.TransactionService;
import com.yaromich.wallet.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepositoryImpl transactionRepository;
    private final UserServiceImpl userService;
    private final AuditServiceImpl auditService;

    @Autowired
    public TransactionServiceImpl(TransactionRepositoryImpl transactionRepository, UserServiceImpl userService, JwtTokenUtil jwtTokenUtil, AuditServiceImpl auditService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.auditService = auditService;
    }

    @Override
    public boolean isTransactionIdUnique(UserDto userDto, String transactionId) throws TransactionException {
        User user = UserMapper.MAPPER.userDtoToUser(userDto);
        if(!transactionRepository.isTransactionIdUnique(user, transactionId)) {
            throw new TransactionIdIsNotUniqueException("Transaction failed. Transaction id is not unique.");
        }
        return transactionRepository.isTransactionIdUnique(user, transactionId);
    }

    @Override
    @Transactional
    public void withdraw(TransactionRequest transactionRequest) throws TransactionException, UserException {
        Transaction transaction = new Transaction();
        UserDto userDto = new UserDto();
        try {
            userDto = userService.findUserByLogin(transactionRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

            transaction.setTransactionId(transactionRequest.getTransactionId());
            transaction.setType(transactionRequest.getType());

            if (isTransactionIdUnique(userDto, transactionRequest.getTransactionId()) && isWithdrawTransactionAllow(userDto, transactionRequest.getAmount())) {
                transaction.setCondition("SUCCESS");
                transaction.setUser(UserMapper.MAPPER.userDtoToUser(userDto));
                transactionRepository.save(transaction);

                BigDecimal currentBalance = userDto.getBalance();
                userDto.setBalance(currentBalance.subtract(transactionRequest.getAmount()));
                userDto.setTransactions(List.of(TransactionMapper.MAPPER.transactionToTransactionDto(transaction)));
                userService.update(userDto);
                saveAudit(transaction, userDto);
            }
        } catch (TransactionException | UserException e) {
            transaction.setCondition("FAIL");
            transaction.setUser(UserMapper.MAPPER.userDtoToUser(userDto));
            transaction.setNote(e.getMessage());
            transactionRepository.save(transaction);
            saveAudit(transaction, userDto);
            throw e;
        }
    }

    private String transactionDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private boolean isWithdrawTransactionAllow(UserDto user, BigDecimal amount) throws TransactionException {
        if(user.getBalance().compareTo(amount) > 0) {
            return true;
        } else {
            throw new NotEnoughMoneyException("Not enough money in your wallet");
        }
    }

    @Override
    @Transactional
    public void replenish(TransactionRequest transactionRequest) throws TransactionException, UserException {
        Transaction transaction = new Transaction();
        UserDto userDto = new UserDto();
        try {
            userDto = userService.findUserByLogin(transactionRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

            transaction.setTransactionId(transactionRequest.getTransactionId());
            transaction.setType(transactionRequest.getType());

            if (isTransactionIdUnique(userDto, transactionRequest.getTransactionId()) && userDto != null) {
                transaction.setCondition("SUCCESS");
                transaction.setUser(UserMapper.MAPPER.userDtoToUser(userDto));
                transactionRepository.save(transaction);

                BigDecimal currentBalance = userDto.getBalance();
                userDto.setBalance(currentBalance.add(transactionRequest.getAmount()));
                userDto.setTransactions(List.of(TransactionMapper.MAPPER.transactionToTransactionDto(transaction)));
                userService.update(userDto);
                saveAudit(transaction, userDto);

            }
        } catch (TransactionException | UserException e) {
            transaction.setCondition("FAIL");
            transaction.setUser(UserMapper.MAPPER.userDtoToUser(userDto));
            transaction.setNote(e.getMessage());
            transactionRepository.save(transaction);
            saveAudit(transaction, userDto);
            throw e;
        }
    }

    private void saveAudit(Transaction transaction, UserDto userDto) {
        AuditDto auditDto = new AuditDto();
        auditDto.setType(transaction.getType());
        auditDto.setDateTime(transactionDateTime());
        auditDto.setStatus(transaction.getCondition());
        auditDto.setBalance(userDto.getBalance());
        auditDto.setNote(transaction.getNote());
        auditDto.setUserDto(userDto);
        auditService.save(userDto.getLogin(), auditDto);
    }

    @Override
    public Map<String, List<TransactionDto>> findAllByUserLogin(String login) throws UserException {
        UserDto userDto = userService.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<TransactionDto> transactions = transactionRepository.findAllByUserLogin(userDto.getLogin())
                .stream().map((TransactionMapper.MAPPER::transactionToTransactionDto)).toList();

        Map<String, List<TransactionDto>> history =  new HashMap<>();
        history.put(login, transactions);
        return history;
    }
}
