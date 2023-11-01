package com.yaromich.wallet.services;

import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.logic.exceptions.UserException;

import java.util.List;

public interface AuditService {
    AuditDto save(String login, AuditDto audit);
    List<AuditDto> findAllByUserName(String login) throws UserException;
}
