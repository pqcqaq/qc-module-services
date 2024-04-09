package online.zust.qcqcqc.services.module.log.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.service.LogService;
import online.zust.qcqcqc.services.utils.SpElParser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
            log.error("Error in method: " + joinPoint.getSignature().getName(), e);
            operatorLog.setSuccess(false);
            if (e instanceof ServiceException se) {
                operatorLog.setCause(se.getMessage());
            } else {
                operatorLog.setCause("未捕获的系统异常信息! --> " + e.getMessage());
            }
            throwable = e;
        }
        // 保存日志
        try {
            // 设置 BeanFactoryResolver，用于解析 SpEL 表达式中的 bean
            final HashMap<String, Object> paramMap = new HashMap<>(args.length + 1);
            for (int i = 0; i < args.length; i++) {
                // 把 Controller 方法中的参数都设置到 context 中，使用参数名称作为 key。
                paramMap.put(parameterNames[i], args[i]);
            }
            // 把方法的返回值也设置到 context 中，使用 returnValue 作为 key。
            paramMap.put("returnValue", proceed);

            // 如果不需要记录日志，则直接执行下去，不需要走下面的日志逻辑
            final String condition = operationLog.condition();
            if (!condition.isEmpty()) {
                // 解析注解上定义的表达式，获取到结果
                final Boolean result = SpElParser.parseExpression(condition, paramMap, Boolean.class);
                if (Boolean.FALSE.equals(result)) {
                    return returnOrThrow(throwable, proceed);
                }
            }
            // 解析注解上定义的表达式，获取到结果
            final String result = SpElParser.parseExpression(operationLog.value(), paramMap, String.class);

            operatorLog.setMsg(result);
        } catch (Exception e) {
            log.error("操作日志SpEL表达式解析异常: {}", e.getMessage());
            operatorLog.setMsg("操作日志SpEL表达式解析异常");
        }
        // 保存日志，如果有错误则继续向上抛出，否则返回结果
        logService.saveAsync(operatorLog);
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
