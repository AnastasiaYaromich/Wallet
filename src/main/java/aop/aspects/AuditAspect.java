package aop.aspects;

import dto.AuditDto;
import dto.TransactionDto;
import infrastructure.out.audit.AuditServiceSingleton;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import services.AuditService;

import java.sql.SQLException;

@Aspect
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect() throws SQLException {
        this.auditService = AuditServiceSingleton.getAuditService();
    }


    @AfterReturning(
            pointcut = "execution(public * logic.TransactionServiceImpl.save(..))",
            returning = "result"
    )
    public void saveAudit(JoinPoint joinPoint, TransactionDto result) {
        AuditDto auditDto = new AuditDto();
        auditDto.setType(result.getType());
        auditDto.setStatus(result.getCondition());
        auditDto.setNote(result.getNote());
        auditService.save((String) joinPoint.getArgs()[0], auditDto);
    }
}
