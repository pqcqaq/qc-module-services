package online.zust.qcqcqc.services.module.log.service;

import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.utils.IServiceEnhance;
import org.intellij.lang.annotations.Language;

/**
 * @author qcqcqc
 */
public interface LogService extends IServiceEnhance<OperatorLog> {
    /**
     * 异步保存操作日志
     *
     * @param operatorLog 操作日志
     */
    void saveAsync(OperatorLog operatorLog);

    /**
     * 保存注解日志
     *
     * @param operationLog   操作日志注解
     * @param args           方法参数
     * @param parameterNames 参数名称
     * @param proceed        方法返回值
     * @param operatorLog    操作日志
     */
    void saveAnnotationLog(final String metadata, final OperationLog operationLog, final Object[] args, final String[] parameterNames, final Object proceed, OperatorLog operatorLog);

    /**
     * 保存日志
     *
     * @param spEl SpEl表达式
     */
    void info(@Language("spel") String spEl);

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     */
    void error(@Language("spel") String spEl);

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    void error(@Language("spel") String spEl, Throwable e);

    /**
     * 保存错误日志
     *
     * @param e 异常
     */
    void error(Throwable e);

    /**
     * 保存错误日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    void error(String spEl, String cause);

    /**
     * 保存调试日志
     *
     * @param spEl SpEl表达式
     */
    void debug(@Language("spel") String spEl);

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     */
    void warn(@Language("spel") String spEl);

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    void warn(@Language("spel") String spEl, Throwable e);

    /**
     * 保存警告日志
     *
     * @param e 异常
     */
    void warn(Throwable e);

    /**
     * 保存警告日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    void warn(String spEl, String cause);

}
