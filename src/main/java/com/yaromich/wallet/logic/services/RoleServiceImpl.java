package com.yaromich.wallet.logic.services;

import com.yaromich.wallet.domain.model.Role;
import com.yaromich.wallet.infrastructure.out.RoleRepositoryImpl;
import com.yaromich.wallet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepositoryImpl roleRepository;

    @Autowired
    private RoleServiceImpl(RoleRepositoryImpl roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
