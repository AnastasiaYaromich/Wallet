package infrastructure.out.transaction;

import aop.annotations.Speed;
import domain.models.Transaction;
import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import infrastructure.out.user.UserRepositorySingleton;
import repositories.TransactionRepository;
import repositories.UserRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Speed
public class TransactionRepositoryImpl implements TransactionRepository {

    private final UserRepository userRepository;

    public static Connection connection;

    static {
        try {
            connection = LiquibaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionRepositoryImpl() throws SQLException {
        this.userRepository = UserRepositorySingleton.getUserRepositoryInstance();
    }

    @Override
    public boolean isTransactionIdUnique(String login, String id) {
        List<String> transactionsIds = new ArrayList<>();
        Integer userId;
        try(PreparedStatement statement = connection.prepareStatement("SELECT transaction_id FROM wallet.transaction WHERE user_id=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                userId = user.get().getId();
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

    @Override
    public boolean withdraw(String login, BigDecimal amount) {
        try(PreparedStatement statement = connection.prepareStatement("UPDATE wallet.users SET balance=? WHERE login=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setBigDecimal(1, user.get().getBalance().subtract(amount));
                statement.setString(2, user.get().getLogin());
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void replenish(String login, BigDecimal amount) {
        try(PreparedStatement statement = connection.prepareStatement("UPDATE wallet.users SET balance=? WHERE login=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setBigDecimal(1,findBalanceByLogin(login).add(amount));
                statement.setString(2, user.get().getLogin());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal findBalanceByLogin(String login) {
        BigDecimal balance = BigDecimal.ZERO;
        try(PreparedStatement statement = connection.prepareStatement("SELECT balance FROM wallet.users WHERE login=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setString(1, login);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    balance = rs.getBigDecimal("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public Transaction save(String login, Transaction transaction) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.transaction" +
                        " (transaction_id, type, condition, note, user_id)" +
                        " VALUES(?, ?, ?, ?, ?)")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setString(1, transaction.getTransactionId());
                statement.setString(2, transaction.getType());
                statement.setString(3, transaction.getCondition());
                statement.setString(4, transaction.getNote());
                statement.setInt(5, user.get().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAll(String login) {
        List<Transaction> transactions = new ArrayList<>();
        try(PreparedStatement statement= connection.prepareStatement("SELECT * FROM wallet.transaction WHERE user_id=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setInt(1, user.get().getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
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
}
