package infrastructure.out.audit;

import aop.annotations.Speed;
import repositories.AuditRepository;

import java.sql.Connection;
import java.sql.SQLException;


@Speed
public class AuditRepositorySingleton {
    private static AuditRepository auditRepositoryInstance;

    public static AuditRepository getAuditRepositoryInstance() throws SQLException {
        if(auditRepositoryInstance == null) {
            auditRepositoryInstance = new AuditRepositoryImpl();
        }
        return auditRepositoryInstance;
    }
}
