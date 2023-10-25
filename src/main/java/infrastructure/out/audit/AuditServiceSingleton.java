package infrastructure.out.audit;
import aop.annotations.Speed;
import logic.AuditServiceImpl;
import services.AuditService;

import java.sql.SQLException;

@Speed
public class AuditServiceSingleton {
    private static AuditService auditServiceInstance;

    public static AuditService getAuditService() throws SQLException {
        if(auditServiceInstance == null) {
            auditServiceInstance = new AuditServiceImpl(AuditRepositorySingleton.getAuditRepositoryInstance());
        }
        return auditServiceInstance;
    }
}
