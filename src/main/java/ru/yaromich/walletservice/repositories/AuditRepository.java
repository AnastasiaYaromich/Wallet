package ru.yaromich.walletservice.repositories;

import org.springframework.stereotype.Repository;
import ru.yaromich.walletservice.domain.models.Audit;

import java.util.List;

@Repository
public interface AuditRepository {
    Audit save(String login, Audit audit);
    List<Audit> findAllByUserName(String login);
}
