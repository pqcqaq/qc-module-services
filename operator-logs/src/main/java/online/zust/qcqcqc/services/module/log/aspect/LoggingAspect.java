package online.zust.qcqcqc.services.module.log.aspect;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 */
@Component
@Aspect
@Slf4j
public class LoggingAspect implements ApplicationContextAware {
    private final LogService logService;

    /**
     * 表达式解析模板，在 {{  }} 中的内容，会被当作 SpEL 表达式进行解析
     */
    private static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = new TemplateParserContext("{{", "}}");
    /**
     * SpEL 表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * SpEL 上下文
     */
    private static final StandardEvaluationContext EVALUATION_CONTEXT = new StandardEvaluationContext();

    static {
        log.info("LoggingAspect loaded");
    }

    public LoggingAspect(ApplicationContext applicationContext, LogService logService) {
        this.logService = logService;
        EVALUATION_CONTEXT.setBeanResolver(new BeanFactoryResolver(applicationContext));
    }

    /**
     * 设置 applicationContext
     *
     * @param applicationContext applicationContext
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        EVALUATION_CONTEXT.setBeanResolver(new BeanFactoryResolver(applicationContext));
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
    public Object log(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        // log
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(operationLog.level());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 方法的参数
        Object[] args = joinPoint.getArgs();

        // 参数的名称
        String[] parameterNames = signature.getParameterNames();

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
                operatorLog.setCause("未捕获的系统异常信息！ -->" + e.getMessage());
            }
            throwable = e;
        }
        // 保存日志
        try {
            for (int i = 0; i < args.length; i++) {
                // 把 Controller 方法中的参数都设置到 context 中，使用参数名称作为 key。
                EVALUATION_CONTEXT.setVariable(parameterNames[i], args[i]);
            }
            // 把方法的返回值也设置到 context 中，使用 returnValue 作为 key。
            EVALUATION_CONTEXT.setVariable("returnValue", proceed);
            // 如果不需要记录日志，则直接执行下去，不需要走下面的日志逻辑
            String condition = operationLog.condition();
            if (!condition.isEmpty()) {
                // 解析注解上定义的表达式，获取到结果
                Boolean result = EXPRESSION_PARSER.parseExpression(condition, TEMPLATE_PARSER_CONTEXT).getValue(EVALUATION_CONTEXT, Boolean.class);
                if (Boolean.FALSE.equals(result)) {
                    return joinPoint.proceed();
                }
            }
            // 解析注解上定义的表达式，获取到结果
            String result = EXPRESSION_PARSER.parseExpression(operationLog.value(), TEMPLATE_PARSER_CONTEXT).getValue(EVALUATION_CONTEXT, String.class);

            operatorLog.setMsg(result);
        } catch (Exception e) {
            log.error("操作日志SpEL表达式解析异常: {}", e.getMessage());
            operatorLog.setMsg("操作日志SpEL表达式解析异常");
        }
        // 保存日志，如果有错误则继续向上抛出，否则返回结果
        logService.saveAsync(operatorLog);
        if (throwable != null) {
            throw throwable;
        }
        return proceed;
    }
}
