package ru.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.example.model.Audit;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AuditRepositoryImpl implements AuditRepository {
    private final Connection connection;

    @Autowired
    public AuditRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    @Override
    public void save(Audit audit) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.audit" +
                " (type, time, status, note, balance, user_id)" +
                " VALUES(?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, audit.getType());
                statement.setString(2, audit.getDateTime());
                statement.setString(3, audit.getStatus());
                statement.setString(4, audit.getNote());
                statement.setBigDecimal(5, audit.getUserDto().getBalance());
                statement.setLong(6, audit.getUserDto().getId());
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
