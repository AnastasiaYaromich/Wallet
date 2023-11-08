package ru.yaromich.walletservice.services;

import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import java.util.List;

@Service
public interface AuditService {
    AuditDto save(String login, AuditDto audit);
    List<AuditDto> findAllByUserName(String login) throws UserException;
}
