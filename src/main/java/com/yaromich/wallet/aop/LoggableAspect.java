package com.yaromich.wallet.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggableAspect {

    @Around("execution(* com.yaromich.wallet.services.UserService.save(..))")
    public Object logInAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Calling method " + joinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + joinPoint.getSignature() + "finished." +
                        " Execution time is " + (endTime - startTime) + "ms");
        return result;
    }
}
