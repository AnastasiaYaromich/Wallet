package services;


import com.yaromich.wallet.domain.model.Transaction;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.logic.exceptions.TransactionException;
import com.yaromich.wallet.logic.exceptions.TransactionIdIsNotUniqueException;
import com.yaromich.wallet.logic.services.TransactionServiceImpl;
import com.yaromich.wallet.repositories.TransactionRepository;
import com.yaromich.wallet.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {
    private TransactionRepository transactionRepository;

    private UserRepository userRepository;
    private TransactionServiceImpl transactionService;
    private User user;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);
    }

    @Test
    public void isTransactionIdUniquePositiveCase() throws TransactionException {
        String login = "test";
        Transaction transaction = createTransaction();
        when(transactionRepository.isTransactionIdUnique(createTestUser(), transaction.getTransactionId())).thenReturn(true);
        Assertions.assertTrue(transactionRepository.isTransactionIdUnique(createTestUser(), transaction.getTransactionId()));
    }

    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(BigDecimal.valueOf(0.0));
        return user;
    }

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("1");
        transaction.setType("replenish");
        transaction.setCondition("success");
        transaction.setNote("");
        return transaction;
    }
}
