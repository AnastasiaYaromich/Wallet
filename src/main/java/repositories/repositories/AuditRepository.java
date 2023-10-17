package repositories.repositories;

import domain.models.Audit;
import java.util.List;

/**
 * The AuditRepositoryI interface
 */
public interface AuditRepository {
    void saveAudit(String login, Audit audit);
    List<Audit> userActionAudit(String login);
}
