package logic;

import domain.models.Audit;
import repositories.repositories.AuditRepository;
import services.services.AuditService;
import java.util.List;

/**
 * The AuditServiceImpl class implements AuditService interface.
 */
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * The saveAudit() method calls a saveAudit() method in AuditRepository implementation.
     * @param login user login.
     * @param audit audit record.
     */
    @Override
    public void saveAudit(String login, Audit audit) {
        auditRepository.saveAudit(login, audit);
    }

    /**
     * The userActionAudit() method calls a userActionAudit() method in AuditRepository implementation.
     * @param login user login.
     * @return list of audit records for user.
     */
    @Override
    public List<Audit> userActionAudit(String login) {
        return auditRepository.userActionAudit(login);
    }
}
