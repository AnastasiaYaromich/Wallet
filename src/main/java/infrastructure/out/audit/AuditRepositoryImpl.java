package infrastructure.out.audit;

import aop.annotations.Speed;
import domain.models.Audit;
import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import infrastructure.out.user.UserRepositorySingleton;
import repositories.AuditRepository;
import repositories.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Speed
public class AuditRepositoryImpl implements AuditRepository {

    private final UserRepository userRepository;

    public static Connection connection;

    static {
        try {
            connection = LiquibaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuditRepositoryImpl() throws SQLException {
        this.userRepository = UserRepositorySingleton.getUserRepositoryInstance();
    }

    @Override
    public Audit save(String login, Audit audit) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.audit" +
                " (type, time, status, note, balance, user_id)" +
                " VALUES(?, ?, ?, ?, ?, ?)")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setString(1, audit.getType());
                statement.setString(2, audit.getDateTime());
                statement.setString(3, audit.getStatus());
                statement.setString(4, audit.getNote());
                statement.setBigDecimal(5, user.get().getBalance());
                statement.setInt(6, user.get().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audit;
    }

    @Override
    public List<Audit> findAll(String login) {
        List<Audit> audits = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.audit WHERE user_id=?")) {
            Optional<User> user = userRepository.findUserByLogin(login);
            if(user.isPresent()) {
                statement.setInt(1, user.get().getId());
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    Audit audit = new Audit();
                    audit.setId(rs.getInt("id"));
                    audit.setType(rs.getString("type"));
                    audit.setDateTime(rs.getString("time"));
                    audit.setStatus(rs.getString("status"));
                    audit.setBalance(rs.getBigDecimal("balance"));
                    audit.setNote(rs.getString("note"));
                    audits.add(audit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audits;
    }
}
