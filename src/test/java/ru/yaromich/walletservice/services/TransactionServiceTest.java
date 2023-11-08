package ru.yaromich.walletservice.services;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.dtos.TransactionRequest;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.mappers.TransactionMapper;
import ru.yaromich.walletservice.domain.mappers.UserMapper;
import ru.yaromich.walletservice.domain.models.Transaction;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.TransactionServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.NotEnoughMoneyException;
import ru.yaromich.walletservice.logic.services.exceptions.TransactionException;
import ru.yaromich.walletservice.logic.services.exceptions.TransactionIdIsNotUniqueException;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionServiceTest {
    private TransactionRepository transactionRepository;
    private UserService userService;
    private UserMapper userMapper;
    private TransactionMapper transactionMapper;
    private TransactionServiceImpl transactionService;
    private UserDto userDto;
    private User user;
    private TransactionDto transactionDto;
    private Transaction transaction;
    private TransactionRequest transactionRequest;

    @BeforeEach
    public void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        userMapper = Mockito.mock(UserMapper.class);
        transactionMapper = Mockito.mock(TransactionMapper.class);
        userService = Mockito.mock(UserService.class);

        transactionService = new TransactionServiceImpl(transactionRepository,
                userService, userMapper, transactionMapper);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setLogin("Cat with the guitar");
        userDto.setPassword("999");
        userDto.setTransactions(List.of());
        userDto.setAudits(List.of());
        userDto.setBalance(BigDecimal.valueOf(100));

        transactionDto = new TransactionDto();
        transactionDto.setTransactionId("1");
        transactionDto.setUserDto(userDto);

        user = new User();
        user.setId(1L);
        user.setLogin(userDto.getLogin());
        user.setPassword(userDto.getPassword());
        user.setBalance(userDto.getBalance());
        user.setTransactions(List.of());

        transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId("2");
        transactionRequest.setUsername("Cat with the guitar");
        transactionRequest.setAmount(BigDecimal.valueOf(150));
        transactionRequest.setType("withdraw");
    }

    @Test
    public void shouldReturnTransactionIdIsNotUniqueException() {
        String message = "Transaction id is not unique";
        Mockito.when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionDto.getTransactionId())).thenReturn(false);

        TransactionIdIsNotUniqueException e = Assertions.assertThrows(TransactionIdIsNotUniqueException.class,
                () -> {transactionService.isTransactionIdUnique(userDto, transactionDto.getTransactionId());
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReturnTransactionIdIsUnique() throws TransactionException {
        Mockito.when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionDto.getTransactionId())).thenReturn(true);
        Assertions.assertTrue(transactionService.isTransactionIdUnique(userDto, transactionDto.getTransactionId()));
    }

    @Test
    public void shouldWithdrawMoneySuccessful() throws UserException, TransactionException {
        Mockito.when(userService.findByLogin(transactionRequest.getUsername())).thenReturn(Optional.of(userDto));
        Mockito.when(transactionMapper.transactionDtoToTransaction(transactionDto)).thenReturn(transaction);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionRequest.getTransactionId())).thenReturn(true);
        transactionService.withdraw(transactionRequest);
        Mockito.verify(transactionRepository).save(transaction);
    }

    @Test
    public void shouldReplenishMoneySuccessful() throws UserException, TransactionException {
        Mockito.when(userService.findByLogin(transactionRequest.getUsername())).thenReturn(Optional.of(userDto));
        Mockito.when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        Mockito.when(transactionMapper.transactionDtoToTransaction(transactionDto)).thenReturn(transaction);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionRequest.getTransactionId())).thenReturn(true);
        transactionService.replenish(transactionRequest);
        Mockito.verify(transactionRepository).save(transaction);
    }

    @Test
    public void shouldNotWithdrawMoneyTransactionIdIsNotUnique() {
        String message = "Transaction id is not unique";
        Mockito.when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionDto.getTransactionId())).thenReturn(false);
        TransactionIdIsNotUniqueException e = Assertions.assertThrows(TransactionIdIsNotUniqueException.class,
                () -> {transactionService.isTransactionIdUnique(userDto, transactionDto.getTransactionId());
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldNotWithdrawMoneyNotEnoughMoney() throws TransactionException {
        String message = "Not enough money in your wallet";
        Mockito.when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        Mockito.when(transactionRepository.isTransactionIdUnique(user, transactionDto.getTransactionId())).thenReturn(true);
        NotEnoughMoneyException e = Assertions.assertThrows(NotEnoughMoneyException.class,
                () -> {transactionService.isWithdrawalAllowed(userDto, transactionRequest.getAmount());
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldReturnAllTransactionsByUserLogin() throws UserException {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Mockito.when(userService.findByLogin(user.getLogin())).thenReturn(Optional.of(userDto));
        Mockito.when(transactionRepository.findAllByUser(user)).thenReturn(transactions);
        List<TransactionDto> transactionDtos = transactionService.findAllByUserLogin(user.getLogin());
        AssertionsForClassTypes.assertThat(transactionDtos.size()).isEqualTo(transactions.size());
    }
}
