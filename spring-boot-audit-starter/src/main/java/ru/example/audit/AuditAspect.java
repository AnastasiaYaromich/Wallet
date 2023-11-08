package ru.example.audit;

import exceptions.TransactionException;
import exceptions.UserException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import ru.example.model.Audit;
import ru.example.repository.AuditRepository;

@Aspect
public class AuditAspect {
    private final AuditRepository auditRepository;

    @Autowired
    public AuditAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }



    @Around("execution(* com.yaromich.wallet.logic.services.UserServiceImpl.authenticate(..))")
    public Object authenticateAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Audit audit = new Audit();
        try {
            result = joinPoint.proceed();
            audit.setStatus("success");
            audit.setNote("authenticate");
            auditRepository.save(audit);
            System.out.println("[SUCCESS]: USER AUTHENTICATED.");
        } catch (UserException e) {
            System.out.println("[FAIL]: AUTHENTICATION FAILED. " + e.getMessage());
            throw e;
        }
        return result;
    }

    @Around("execution(* com.yaromich.wallet.logic.services.UserServiceImpl.save(..))")
    public Object saveAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println("[SUCCESS]: USER REGISTERED.");
        } catch (UserException e) {
            System.out.println("[FAIL]: REGISTRATION FAILED." + e.getMessage());
            throw e;
        }
        return result;
    }

    @Around("execution(* com.yaromich.wallet.logic.services.TransactionServiceImpl.withdraw(..))")
    public Object withdrawAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println("[SUCCESS]: WITHDRAWAL PERFORMED.");
        } catch (TransactionException e) {
            System.out.println("[FAIL]: WITHDRAWAL TRANSACTION FAILED. " + e.getMessage());
            throw e;
        }
        return result;
    }

    @Around("execution(* com.yaromich.wallet.logic.services.TransactionServiceImpl.replenish(..))")
    public Object replenishAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println("[SUCCESS]: REPLENISH PERFORMED.");
        } catch (TransactionException e) {
            System.out.println("[FAIL]: REPLENISH TRANSACTION FAILED. " + e.getMessage());
            throw e;
        }
        return result;
    }

    @Around("execution(* com.yaromich.wallet.logic.services.TransactionServiceImpl.findAllByUserLogin(..))")
    public Object findAllByUserLoginAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            System.out.println("[SUCCESS]: HISTORY HAS BEEN RECEIVED.");
        } catch (UserException e) {
            System.out.println("[FAIL]: HISTORY CAN'T RECEIVED. USER IS NOT REGISTERED");
            throw e;
        }
        return result;
    }
}
