package infrastructure.in;

import domain.models.Audit;
import domain.models.Transaction;
import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import repositories.repositories.AuditRepository;
import repositories.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The AuditInMemory class implements AuditRepository interface.
 */
public class AuditInMemory implements AuditRepository {

    private final UserRepository userRepository;

    public AuditInMemory() {
        this.userRepository = UserRepositorySingleton.getUserRepositoryInstance();
    }

    /**
     * The saveAudit() method save audit record in storage.
     * @param login user login.
     * @param audit user action audit record.
     */
    @Override
    public void saveAudit(String login, Audit audit) {
        try(Connection connection = LiquibaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.audit" +
                    " (type, time, status, note, balance, user_id)" +
                    " VALUES(?, ?, ?, ?, ?, ?)")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setString(1, audit.getType());
                statement.setString(2, audit.getDateTime());
                statement.setString(3, audit.getStatus());
                statement.setString(4, audit.getNote());
                statement.setDouble(5, user.getBalance());
                statement.setInt(6, user.getId());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The userActionAudit() search and return all audit records for user.
     * @param login user login.
     * @return list of audit records for user.
     */
    @Override
    public List<Audit> userActionAudit(String login) {
        List<Audit> audits = new ArrayList<>();

        try(Connection connection = LiquibaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.audit WHERE user_id=?")) {

            User user = userRepository.findUserByLogin(login);
            if(user != null) {
                statement.setInt(1, user.getId());
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    Audit audit = new Audit();
                    audit.setId(rs.getInt("id"));
                    audit.setType(rs.getString("type"));
                    audit.setDateTime(rs.getString("time"));
                    audit.setStatus(rs.getString("status"));
                    audit.setBalance(rs.getDouble("balance"));
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
