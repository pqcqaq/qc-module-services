package online.zust.qcqcqc.services.module.log.annotation;

import online.zust.qcqcqc.services.module.log.enums.LogLevel;

import java.lang.annotation.*;

/**
 * @author qcqcqc
 * @date 2024/4/8
 * @time 10:15
 * 操作日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface OperationLog {
    /**
     * 日志级别
     *
     * @return 日志级别
     */
    LogLevel level() default LogLevel.INFO;

    /**
     * 日志描述,在参数中可以使用SpEl表达式
     *
     * @return 日志描述
     */
    String value();

    /**
     * 是否记录请求参数
     * SpEl表达式
     *
     * @return 是否记录请求参数
     */
    String condition() default "";
}
