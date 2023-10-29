package services;

import dto.AuditDto;
import java.util.List;

public interface AuditService {
    AuditDto save(String login, AuditDto audit);
    List<AuditDto> findAll(String login);
}
