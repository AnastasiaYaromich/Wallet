package infrastructure.in;

import domain.models.User;
import infrastructure.out.LiquibaseConnection;
import repositories.repositories.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserInMemory class implements UserRepository interface.
 */
public class UserInMemory implements UserRepository {


    public static Connection connection;

    static {
        try {
            connection = LiquibaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The findUserByLogin method used to search a user in storage by passed key.
     * @param login user login.
     * @return method return the user if it was found.
     */
    public User findUserByLogin(String login) {
        User user = null;
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users WHERE login=?")) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setBalance(Double.parseDouble(rs.getString(4)));
                user.setRole(rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * The addUser() method used to add user to storage.
     * @param user user to save.
     * @return method return the user that was saved in storage.
     */
    public User addUser(User user) {
        try(
        PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.users (login, password, balance, role)" +
                " VALUES(?, ?, ?, ?)")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setDouble(3,user.getBalance());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * The users() method used to return a list of users in storage.
     * @return list of users which contains in storage.
     */
    @Override
    public List<User> users() {
        List<User> users = new ArrayList<>();

        try(
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users")) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setBalance(Double.parseDouble(rs.getString("balance")));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
