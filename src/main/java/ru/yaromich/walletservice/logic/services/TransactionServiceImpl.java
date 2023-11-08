package ru.yaromich.walletservice.logic.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.dtos.TransactionRequest;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.mappers.TransactionMapper;
import ru.yaromich.walletservice.domain.mappers.UserMapper;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.exceptions.*;
import ru.yaromich.walletservice.repositories.TransactionRepository;
import ru.yaromich.walletservice.services.TransactionService;
import ru.yaromich.walletservice.services.UserService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, UserMapper userMapper, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public boolean isTransactionIdUnique(UserDto userDto, String transactionId) throws TransactionException {
        if(!transactionRepository.isTransactionIdUnique(UserMapper.MAPPER.userDtoToUser(userDto), transactionId)) {
            throw new TransactionIdIsNotUniqueException("Transaction id is not unique");
        }
        return true;
    }

    @Override
    @Transactional
    public void withdraw(TransactionRequest transactionRequest) throws TransactionException, UserException {
        UserDto user = userService.findByLogin(transactionRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionRequest.getTransactionId());
        transaction.setType(transactionRequest.getType());

        try {
            if(isTransactionIdUnique(user, transactionRequest.getTransactionId()) &&
            isWithdrawalAllowed(user, transactionRequest.getAmount())) {
                transaction.setCondition("SUCCESS");

                user.setBalance(user.getBalance().subtract(transactionRequest.getAmount()));
                user.getTransactions().add(TransactionMapper.MAPPER.transactionToTransactionDto(transaction));
                userService.update(user);

                transaction.setUser(UserMapper.MAPPER.userDtoToUser(user));
                transactionRepository.save(transaction);
            }
        } catch (TransactionException e) {
            transaction.setCondition("FAIL");
            transaction.setUser(UserMapper.MAPPER.userDtoToUser(user));
            transaction.setNote(e.getMessage());
            transactionRepository.save(transaction);
            throw e;
        }
    }

    @Override
    public void replenish(TransactionRequest transactionRequest) throws TransactionException, UserException {
        User user = UserMapper.MAPPER.userDtoToUser(userService.findByLogin(transactionRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found")));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionRequest.getTransactionId());
        transaction.setType(transactionRequest.getType());

        if (transactionRepository.isTransactionIdUnique(user, transactionRequest.getTransactionId())) {
            transaction.setCondition("SUCCESS");

            user.setBalance(user.getBalance().add(transactionRequest.getAmount()));
            user.getTransactions().add(transaction);
            userService.update(UserMapper.MAPPER.userToUserDto(user));

            transaction.setUser(user);
            transactionRepository.save(transaction);
        } else {
            transaction.setCondition("FAIL");
            transaction.setUser(user);
            transaction.setNote("Transaction id is not unique");
            transactionRepository.save(transaction);
            throw new TransactionIdIsNotUniqueException("Transaction id is not unique");
        }
    }

    @Override
    public List<TransactionDto> findAllByUserLogin(String login) throws UserException {
        UserDto userDto = userService.findByLogin(login).get();

        return transactionRepository.findAllByUser(UserMapper.MAPPER.userDtoToUser(userDto))
                .stream().map(TransactionMapper.MAPPER::transactionToTransactionDto).collect(Collectors.toList());
    }

    public boolean isWithdrawalAllowed(UserDto user, BigDecimal amount) throws TransactionException {
        if(user.getBalance().compareTo(amount) > 0) {
            return true;
        } else {
            throw new NotEnoughMoneyException("Not enough money in your wallet");
        }
    }
}
