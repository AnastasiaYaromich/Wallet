package com.yaromich.wallet.repositories;

import com.yaromich.wallet.domain.model.Audit;

import java.util.List;

public interface AuditRepository {
    Audit save(String login, Audit audit);
    List<Audit> findAllByUserName(String login);
}
