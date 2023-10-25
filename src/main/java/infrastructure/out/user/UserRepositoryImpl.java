package infrastructure.out.user;

import aop.annotations.Speed;
import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import repositories.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Speed
public class UserRepositoryImpl implements UserRepository {

    public static Connection connection;

    static {
        try {
            connection = LiquibaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users WHERE login=?")) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setBalance(rs.getBigDecimal(4));
                user.setRole(rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users")) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getBigDecimal("balance"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User user) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.users (login, password, balance, role) VALUES(?, ?, ?, ?)")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setBigDecimal(3, user.getBalance());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
