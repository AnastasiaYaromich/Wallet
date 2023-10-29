import domain.models.Transaction;
import domain.models.User;
import logic.TransactionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repositories.TransactionRepository;
import repositories.UserRepository;
import services.exceptions.TransactionException;
import services.exceptions.TransactionIdIsNotUniqueException;
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
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    public void isTransactionIdUniquePositiveCase() throws TransactionException {
        String login = "test";
        Transaction transaction = createTransaction();
        when(transactionRepository.isTransactionIdUnique(login, transaction.getTransactionId())).thenReturn(true);
        Assertions.assertTrue(transactionRepository.isTransactionIdUnique(login, transaction.getTransactionId()));
    }

    @Test
    public void isTransactionIdUniqueNegativeCase() throws TransactionException {
        String login = "test";
        Transaction transaction = createTransaction();
        when(transactionRepository.isTransactionIdUnique(login, transaction.getTransactionId())).thenReturn(false);
        assertThrows(TransactionIdIsNotUniqueException.class, () -> transactionService.isTransactionIdUnique(login, transaction.getTransactionId()));
    }


    private User createTestUser() {
        User user = new User();
        user.setLogin("someUser");
        user.setPassword("999999");
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRole("user");
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
