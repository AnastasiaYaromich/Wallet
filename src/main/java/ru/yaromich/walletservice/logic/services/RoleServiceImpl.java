package ru.yaromich.walletservice.logic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.infrastructure.out.RoleRepositoryImpl;
import ru.yaromich.walletservice.services.RoleService;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepositoryImpl roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepositoryImpl roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
