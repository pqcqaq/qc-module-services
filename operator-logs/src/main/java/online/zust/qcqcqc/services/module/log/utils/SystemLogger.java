package online.zust.qcqcqc.services.module.log.utils;

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
    private static LogService getBean() {
        if (logService == null) {
            logService = ProxyUtil.getBean(LogService.class);
        }
        return logService;
    }

    /**
     * 保存日志
     *
     * @param spEl SpEl表达式
     */
    public static void info(@Language("spel") String spEl) {
        getBean().info(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     */
    public static void error(@Language("spel") String spEl) {
        getBean().error(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public static void error(@Language("spel") String spEl, Throwable e) {
        getBean().error(spEl, e);
    }

    /**
     * 保存错误日志
     *
     * @param e 异常
     */
    public static void error(Throwable e) {
        getBean().error(e);
    }

    /**
     * 保存错误日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public static void error(String spEl, String cause) {
        getBean().error(spEl, cause);
    }

    /**
     * 保存调试日志
     *
     * @param spEl SpEl表达式
     */
    public static void debug(@Language("spel") String spEl) {
        getBean().debug(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     */
    public static void warn(@Language("spel") String spEl) {
        getBean().warn(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public static void warn(@Language("spel") String spEl, Throwable e) {
        getBean().warn(spEl, e);
    }

    /**
     * 保存警告日志
     *
     * @param e 异常
     */
    public static void warn(Throwable e) {
        getBean().warn(e);
    }

    /**
     * 保存警告日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public static void warn(String spEl, String cause) {
        getBean().warn(spEl, cause);
    }
}
