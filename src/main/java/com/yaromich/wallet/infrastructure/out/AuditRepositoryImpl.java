package com.yaromich.wallet.infrastructure.out;

import com.yaromich.wallet.domain.model.Audit;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.repositories.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuditRepositoryImpl implements AuditRepository {

    private final Connection connection;
    private final UserRepositoryImpl userRepository;

    @Autowired
    public AuditRepositoryImpl(DataSource dataSource, UserRepositoryImpl userRepository) throws SQLException {
        this.userRepository = userRepository;
        this.connection = dataSource.getConnection();
    }

    @Override
    public Audit save(String login, Audit audit) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.audit" +
                    " (type, time, status, note, balance, user_id)" +
                    " VALUES(?, ?, ?, ?, ?, ?)")) {
            Optional<User> user = userRepository.findByLogin(login);
            if(user.isPresent()) {
                statement.setString(1, audit.getType());
                statement.setString(2, audit.getDateTime());
                statement.setString(3, audit.getStatus());
                statement.setString(4, audit.getNote());
                statement.setBigDecimal(5, user.get().getBalance());
                statement.setLong(6, user.get().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audit;
    }

    @Override
    public List<Audit> findAllByUserName(String login) {
        List<Audit> audits = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.audit WHERE user_id=?")) {
            Optional<User> user = userRepository.findByLogin(login);
            if(user.isPresent()) {
                statement.setLong(1, user.get().getId());
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    Audit audit = new Audit();
                    audit.setId(rs.getInt("id"));
                    audit.setType(rs.getString("type"));
                    audit.setDateTime(rs.getString("time"));
                    audit.setStatus(rs.getString("status"));
                    audit.setBalance(rs.getBigDecimal("balance"));
                    audit.setNote(rs.getString("note"));
                    audit.setUser(user.get());
                    audits.add(audit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audits;
    }
}
