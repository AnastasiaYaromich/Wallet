package ru.yaromich.walletservice.services;

import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.models.Role;
import java.util.Optional;

@Service
public interface RoleService {
    Optional<Role> findByName(String name);
}
