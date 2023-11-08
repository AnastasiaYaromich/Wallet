package ru.yaromich.walletservice.repositories;

import org.springframework.stereotype.Repository;
import ru.yaromich.walletservice.domain.models.Role;
import java.util.List;

@Repository
public interface UserRoleRepository {
    void save( Long userId, Long roleId);
    List<Role> findAllByUserId(Long userId);
}
