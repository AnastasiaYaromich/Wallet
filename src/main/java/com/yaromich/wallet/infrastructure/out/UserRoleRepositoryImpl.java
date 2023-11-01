package com.yaromich.wallet.infrastructure.out;

import com.yaromich.wallet.domain.model.Role;
import com.yaromich.wallet.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private final Connection connection;
    private final RoleRepositoryImpl roleRepository;

    @Autowired
    public UserRoleRepositoryImpl(DataSource dataSource, RoleRepositoryImpl roleRepository) throws SQLException {
        this.connection = dataSource.getConnection();

        this.roleRepository = roleRepository;
    }

    @Override
    public void save( Long userId, Long roleId) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet.users_roles (user_id, role_id) VALUES(?, ?)")) {
            statement.setLong(1, userId);
            statement.setLong(2, roleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findAllByUserId(Long userId) {
        List<Role> roles = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.users_roles WHERE user_id=?")) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong("role_id");
                if(roleRepository.findById(id).isPresent()) {
                    Role role = roleRepository.findById(id).get();
                    roles.add(role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

}
