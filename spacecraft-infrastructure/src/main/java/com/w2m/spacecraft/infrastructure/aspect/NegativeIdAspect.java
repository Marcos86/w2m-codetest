package com.w2m.spacecraft.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class NegativeIdAspect {


    @Around("@annotation(NegativeId)")
    public Object logNegativeId(ProceedingJoinPoint joinPoint) throws Throwable {

        var args = joinPoint.getArgs();
        try {
            var identifier = Long.valueOf(args[0].toString());
            if (identifier<0) {
                log.info("Negative identifier found with value: {}", identifier);
            }
        } catch (Exception e) {
            log.error("Unable to parse identifier selected with value: {}", args[0]);
        }

        return joinPoint.proceed();
    }

}