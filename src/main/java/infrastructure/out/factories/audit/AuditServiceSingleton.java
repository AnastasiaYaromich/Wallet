package infrastructure.out.factories.audit;

import infrastructure.in.AuditRepositorySingleton;
import logic.AuditServiceImpl;
import services.services.AuditService;

public class AuditServiceSingleton {
    private static AuditService auditServiceInstance;

    /**
     * @return AuditService implementation
     */
    public static AuditService getAuditService() {
        if(auditServiceInstance == null) {
            auditServiceInstance = new AuditServiceImpl(AuditRepositorySingleton.getAuditRepositoryInstance());
        }
        return auditServiceInstance;
    }
}
