package com.yaromich.wallet.repositories;

import com.yaromich.wallet.domain.model.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
    Optional<Role> findById(Long id);
}
