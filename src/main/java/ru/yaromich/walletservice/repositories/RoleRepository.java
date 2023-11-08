package ru.yaromich.walletservice.repositories;

import org.springframework.stereotype.Repository;
import ru.yaromich.walletservice.domain.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<Role> findByName(String name);
    Optional<Role> findById(Long id);
}
