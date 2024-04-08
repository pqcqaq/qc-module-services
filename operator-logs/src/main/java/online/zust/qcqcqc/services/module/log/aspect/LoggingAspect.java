package online.zust.qcqcqc.services.module.log.aspect;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(online.zust.qcqcqc.services.module.log.annotation.OperationLog)")
    public void method() {
    }

    @Around("method() && @annotation(operationLog)")
    public Object log(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error in method: " + joinPoint.getSignature().getName(), e);
            throw e;
        }
        return proceed;
    }
}
