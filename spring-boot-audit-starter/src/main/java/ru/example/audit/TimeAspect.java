package ru.example.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class TimeAspect {
    @Around("execution(* ru.yaromich..*.*(..))")
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
