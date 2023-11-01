package com.yaromich.wallet.infrastructure.out;

import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.repositories.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;
    private final RoleRepositoryImpl roleRepository;
    private final UserRoleRepositoryImpl userRoleRepository;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource, RoleRepositoryImpl roleRepository, UserRoleRepositoryImpl userRoleRepository) throws SQLException {
        this.connection = dataSource.getConnection();
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users WHERE login=?")) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong(1));
                user.setLogin(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setBalance(rs.getBigDecimal(4));
                user.setRoles(userRoleRepository.findAllByUserId(user.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.users (login, password, balance) VALUES(?, ?, ?)")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setBigDecimal(3, user.getBalance());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void update(User user) {
        try(PreparedStatement statement = connection.prepareStatement("UPDATE wallet.users SET balance=? WHERE login=?")) {
                statement.setBigDecimal(1, user.getBalance());
                statement.setString(2, user.getLogin());
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users")) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getBigDecimal("balance"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
