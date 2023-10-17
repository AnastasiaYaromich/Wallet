package infrastructure.in;

import domain.models.Transaction;
import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import repositories.repositories.TransactionRepository;
import repositories.repositories.UserRepository;
import services.exceptions.MainException;
import services.exceptions.transaction.NotEnoughMoneyException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The TransactionInMemory class implements TransactionRepository interface.
 */
public class TransactionInMemory implements TransactionRepository {

    private final UserRepository userRepository;

    public TransactionInMemory() {
        this.userRepository = UserRepositorySingleton.getUserRepositoryInstance();
    }

    /**
     * The isTransactionIdUnique() method check if user enter an unique transaction id.
     * @param login user login.
     * @param id transaction id.
     * @return true if transaction id is unique or false if it is not.
     */
    public boolean isTransactionIdUnique(String login, String id)  {
        List<String> transactionsIds = new ArrayList<>();
        Integer userId;

        try(Connection connection = LiquibaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT transaction_id FROM wallet.transaction WHERE user_id=?")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                userId = user.getId();
                statement.setInt(1, userId);
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String transactionId = rs.getString("transaction_id");
                transactionsIds.add(transactionId);
            }

            for (String transactionId : transactionsIds) {
                if (transactionId.equals(id)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * The saveTransaction() method save user transaction in storage.
     * @param login user login.
     * @param id transaction id.
     * @param type transaction type (withdraw/replenish).
     * @param condition result of transaction (success/fail)
     * @param note reason why transaction was failed if it was.
     */
    @Override
    public void saveTransaction(String login, String id, String type, String condition, String note) {
        try(Connection connection = LiquibaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.transaction" +
                    " (transaction_id, type, condition, note, user_id)" +
                    " VALUES(?, ?, ?, ?, ?)")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setString(1, id);
                statement.setString(2, type);
                statement.setString(3, condition);
                statement.setString(4, note);
                statement.setInt(5, user.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The receiveTransactionsHistory() method returns a transactions history for user.
     * @param login user login.
     * @return transaction history for user.
     */
    @Override
    public List<Transaction> receiveTransactionsHistory(String login) {
        List<Transaction> transactions = new ArrayList<>();

        try(Connection connection = LiquibaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement("SELECT * FROM wallet.transaction WHERE user_id=?")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setString(1, String.valueOf(user.getId()));
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setId(Integer.parseInt(rs.getString("id")));
                    transaction.setTransactionId(rs.getString("transaction_id"));
                    transaction.setCondition(rs.getString("condition"));
                    transaction.setType(rs.getString("type"));
                    transaction.setNote(rs.getString("note"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * The withdrawal() method make a withdrawal transaction.
     * Checks if user has enough money to withdraw.
     * @param amount amount of money to withdraw.
     * @return true if transaction was successful or false if it wasn't.
     */
    @Override
    public boolean withdraw(String login, double amount) throws MainException {
        try(Connection connection = LiquibaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE wallet.users SET balance=? WHERE login=?")) {

            User user = userRepository.findUserByLogin(login);

            if(user != null) {
                if (currentBalance(login) >= amount) {
                    statement.setDouble(1, (user.getBalance() - amount));
                    statement.setString(2, user.getLogin());
                    statement.executeUpdate();
                    return true;
                } else {
                    throw new NotEnoughMoneyException("Not enough money in your wallet");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * To replenish() method make a replenish transaction.
     * @param amount amount of money to replenish.
     */
    @Override
    public void replenish(String login, double amount) {

        try(Connection connection = LiquibaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE wallet.users SET balance=? WHERE login=?")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setDouble(1,currentBalance(login) + amount);
                statement.setString(2, user.getLogin());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double currentBalance(String login) {
        Double balance = 0.0;
        try(Connection connection = LiquibaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT balance FROM wallet.users WHERE login=?")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setString(1, login);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    balance = Double.valueOf(rs.getString("balance"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
}
