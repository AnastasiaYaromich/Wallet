package logic;

import domain.models.Transaction;
import domain.models.User;
import repositories.repositories.TransactionRepository;
import services.exceptions.MainException;
import services.exceptions.transaction.NotEnoughMoneyException;
import services.exceptions.transaction.TransactionIdIsNotUniqueException;
import services.services.TransactionService;

import java.util.List;

/**
 * The TransactionServiceImpl class implements TransactionService interface.
 */
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepositoryI) {
        this.transactionRepository = transactionRepositoryI;
    }

    /**
     * The isTransactionIdUnique() method calls isTransactionIdUnique() method in TransactionRepositoryI implementation.
     * @param login user login.
     * @param id transaction id.
     * @return true if transaction id is unique or TransactionIdIsNotUniqueException if it is not.
     * @throws MainException throws if transaction id is nit unique.
     */
    @Override
    public boolean isTransactionIdUnique(String login, String id) throws MainException {
        if(!transactionRepository.isTransactionIdUnique(login, id)) {
            throw new TransactionIdIsNotUniqueException("Transaction id is not unique");
        }
        return transactionRepository.isTransactionIdUnique(login, id);
    }

    /**
     * The saveTransaction() method calls saveTransaction() method in TransactionRepositoryI implementation.
     * @param login user login.
     * @param id transaction id.
     * @param type transaction type (withdraw/replenish).
     * @param condition result of transaction (success/fail)
     * @param note reason why transaction was failed if it was.
     * @throws MainException throws if transaction wasn't saved.
     */
    @Override
    public void saveTransaction(String login, String id, String type, String condition, String note) throws MainException {
        transactionRepository.saveTransaction(login, id, type, condition, note);
    }

    /**
     * The receiveTransactionsHistory() method calls receiveTransactionsHistory() method in TransactionRepositoryI implementation.
     * @param login user login.
     * @return transaction history for user.
     */
    @Override
    public List<Transaction> receiveTransactionsHistory(String login) {
        return transactionRepository.receiveTransactionsHistory(login);
    }

    /**
     * The withdrawal() method calls withdrawal() method in TransactionRepositoryI implementation.
     *
     * @param amount amount of money to withdraw.
     * @return true if transaction was successful or NotEnoughMoneyException if it wasn't.
     * @throws MainException throws if user doesn't have enough money to withdraw.
     */
    @Override
    public boolean withdraw(String login, double amount) throws MainException {
        return transactionRepository.withdraw(login, amount);
    }

    /**
     * To replenish() method calls replenish() method in TransactionRepositoryI implementation.
     * @param amount amount of money to replenish.
     * @throws MainException throws if transaction wasn't success.
     */
    @Override
    public void replenish(String login, double amount) throws MainException {
        transactionRepository.replenish(login, amount);
    }

    @Override
    public Double currentBalance(String login) {
        return transactionRepository.currentBalance(login);
    }
}
