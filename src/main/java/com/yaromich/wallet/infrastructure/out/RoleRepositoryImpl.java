package com.yaromich.wallet.infrastructure.out;

import com.yaromich.wallet.domain.model.Role;
import com.yaromich.wallet.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final Connection connection;

    @Autowired
    public RoleRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    @Override
    public Optional<Role> findByName(String name) {
        Role role = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.roles WHERE name=?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                role = new Role();
                role.setId(rs.getLong(1));
                role.setName(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        Role role = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet.roles WHERE id=?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                role = new Role();
                role.setId(rs.getLong(1));
                role.setName(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(role);
    }
}
