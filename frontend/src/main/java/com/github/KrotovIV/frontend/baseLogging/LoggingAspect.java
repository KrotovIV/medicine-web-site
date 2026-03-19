package com.github.KrotovIV.frontend.baseLogging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// Создаем аспект
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(LoggingDecorator)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("====================");
        var methodName = joinPoint.getSignature().getName();
        System.out.println("METHOD: " + methodName);
        System.out.println();

        Object result = joinPoint.proceed(); // выполняем метод

        System.out.println();
        System.out.println("Method finished");
        System.out.println("====================");

        return result;
    }
}
