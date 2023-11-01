package com.yaromich.wallet.repositories;

import com.yaromich.wallet.domain.model.Role;

import java.util.List;

public interface UserRoleRepository {
    void save( Long userId, Long roleId);
    List<Role> findAllByUserId(Long userId);
}
