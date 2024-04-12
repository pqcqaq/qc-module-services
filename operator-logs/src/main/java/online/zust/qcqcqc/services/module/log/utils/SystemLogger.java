package online.zust.qcqcqc.services.module.log.utils;

import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.service.LogService;
import online.zust.qcqcqc.utils.utils.ProxyUtil;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

/**
 * @author qcqcqc
 * Date: 2024/4/10
 * Time: 17:54
 */
public class SystemLogger {

    /**
     * 日志服务
     */
    private static LogService logService;

    @NotNull
    private static LogService getLoggerService() {
        if (logService == null) {
            logService = ProxyUtil.getBean(LogService.class);
        }
        return logService;
    }

    /**
     * 手动保存日志
     *
     * @param operatorLog 操作日志
     */
    public static void saveLog(OperatorLog operatorLog) {
        getLoggerService().saveAsync(operatorLog);
    }

    /**
     * 保存日志
     *
     * @param spEl SpEl表达式
     */
    public static void info(@Language("spel") String spEl) {
        getLoggerService().info(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     */
    public static void error(@Language("spel") String spEl) {
        getLoggerService().error(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public static void error(@Language("spel") String spEl, Throwable e) {
        getLoggerService().error(spEl, e);
    }

    /**
     * 保存错误日志
     *
     * @param e 异常
     */
    public static void error(Throwable e) {
        getLoggerService().error(e);
    }

    /**
     * 保存错误日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public static void error(String spEl, String cause) {
        getLoggerService().error(spEl, cause);
    }

    /**
     * 保存调试日志
     *
     * @param spEl SpEl表达式
     */
    public static void debug(@Language("spel") String spEl) {
        getLoggerService().debug(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     */
    public static void warn(@Language("spel") String spEl) {
        getLoggerService().warn(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public static void warn(@Language("spel") String spEl, Throwable e) {
        getLoggerService().warn(spEl, e);
    }

    /**
     * 保存警告日志
     *
     * @param e 异常
     */
    public static void warn(Throwable e) {
        getLoggerService().warn(e);
    }

    /**
     * 保存警告日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public static void warn(String spEl, String cause) {
        getLoggerService().warn(spEl, cause);
    }
}
