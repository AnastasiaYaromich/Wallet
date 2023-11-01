package com.yaromich.wallet.infrastructure.out;

import com.yaromich.wallet.domain.model.Transaction;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Connection connection;
    private final UserRepositoryImpl userRepository;

    @Autowired
    public TransactionRepositoryImpl(DataSource dataSource, UserRepositoryImpl userRepository) throws SQLException {
        this.connection = dataSource.getConnection();
        this.userRepository = userRepository;
    }

    @Override
    public boolean isTransactionIdUnique(User user, String transactionId) {
        List<String> ids = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT transaction_id FROM wallet.transaction WHERE user_id=?")) {
            statement.setLong(1, user.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id= rs.getString("transaction_id");
                ids.add(id);
            }
            for (String id : ids) {
                if (id.equals(transactionId)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.transaction" +
                    " (transaction_id, type, condition, note, user_id)" +
                    " VALUES(?, ?, ?, ?, ?)")) {
                statement.setString(1, transaction.getTransactionId());
                statement.setString(2, transaction.getType());
                statement.setString(3, transaction.getCondition());
                statement.setString(4, transaction.getNote());
                statement.setLong(5, transaction.getUser().getId());
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAllByUserLogin(String login) {
        List<Transaction> transactions = new ArrayList<>();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try(PreparedStatement statement= connection.prepareStatement("SELECT * FROM wallet.transaction WHERE user_id=?")) {
                statement.setLong(1, user.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setId(Integer.parseInt(rs.getString("id")));
                    transaction.setTransactionId(rs.getString("transaction_id"));
                    transaction.setCondition(rs.getString("condition"));
                    transaction.setType(rs.getString("type"));
                    transaction.setNote(rs.getString("note"));
                    transaction.setUser(user);
                    transactions.add(transaction);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
