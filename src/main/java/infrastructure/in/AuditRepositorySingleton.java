package infrastructure.in;

import repositories.repositories.AuditRepository;

public class AuditRepositorySingleton {
    private static AuditRepository auditRepositoryInstance;

    /**
     * @return  AuditRepository implementation
     */
    public static AuditRepository getAuditRepositoryInstance() {
        if(auditRepositoryInstance == null) {
            auditRepositoryInstance = new AuditInMemory();
        }
        return auditRepositoryInstance;
    }
}
