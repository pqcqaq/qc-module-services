package online.zust.qcqcqc.services.module.log.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;
import online.zust.qcqcqc.services.module.log.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 */
@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class LoggingAspect {
    private final LogService logService;

    static {
        log.info("LoggingAspect loaded");
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(online.zust.qcqcqc.services.module.log.annotation.OperationLog)")
    public void method() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint    joinPoint
     * @param operationLog operationLog
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("method() && @annotation(operationLog)")
    public Object log(final ProceedingJoinPoint joinPoint, final OperationLog operationLog) throws Throwable {
        // log
        final OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(operationLog.level());

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 方法的参数
        final Object[] args = joinPoint.getArgs();

        // 参数的名称
        final String[] parameterNames = signature.getParameterNames();

        // 运行结果
        Object proceed = null;
        Throwable throwable = null;
        try {
            proceed = joinPoint.proceed();
            operatorLog.setSuccess(true);
            operatorLog.setCause("无异常信息");
        } catch (Throwable e) {
            log.warn("Error in method: {} ,error info: {}", joinPoint.getSignature().getName(), e.getMessage());
            operatorLog.setSuccess(false);
            operatorLog.setCause(e.getMessage());
            operatorLog.setLevel(LogLevel.WARN);
            throwable = e;
        }
        logService.saveAnnotationLog(operationLog, args, parameterNames, proceed, operatorLog);
        return returnOrThrow(throwable, proceed);
    }

    /**
     * 返回结果或者抛出异常
     *
     * @param throwable 异常
     * @param proceed   结果
     * @return 结果
     * @throws Throwable 异常
     */
    private static Object returnOrThrow(Throwable throwable, Object proceed) throws Throwable {
        if (throwable != null) {
            throw throwable;
        }
        return proceed;
    }
}
