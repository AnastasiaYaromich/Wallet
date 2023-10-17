package services.services;

import domain.models.Audit;
import java.util.List;

/**
 * The AuditService interface
 */
public interface AuditService {
    void saveAudit(String login, Audit audit);
    List<Audit> userActionAudit(String login);
}
