package ru.gb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Before("within(ru.gb..*)")
    public void methodsLogging(JoinPoint jp) {
        StringBuilder args = new StringBuilder();

        MethodSignature methodSignature = (MethodSignature) jp.getSignature();

        var parameterTypes = methodSignature.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {
            args.append(parameterTypes[i].getSimpleName())
                    .append(" = ")
                    .append(jp.getArgs()[i])
                    .append(", ");
        }

        if (!args.isEmpty()) {
            args.delete(args.length() - 2, args.length());
        }

        String targetClassName = jp.getTarget().getClass().getSimpleName();
        String targetMethodName = jp.getSignature().getName();

        log.info("Log: {}.{}({})", targetClassName, targetMethodName, args);
    }

}
