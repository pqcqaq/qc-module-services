package online.zust.qcqcqc.services.module.log.utils;

import lombok.RequiredArgsConstructor;
import online.zust.qcqcqc.services.module.log.service.LogService;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 * Date: 2024/4/10
 * Time: 17:54
 */
@Component
@RequiredArgsConstructor
public class SystemLogger {
    private final LogService logService;

    /**
     * 保存日志
     *
     * @param spEl SpEl表达式
     */
    public void info(@Language("spel") String spEl) {
        logService.info(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     */
    public void error(@Language("spel") String spEl) {
        logService.error(spEl);
    }

    /**
     * 保存错误日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public void error(@Language("spel") String spEl, Throwable e) {
        logService.error(spEl, e);
    }

    /**
     * 保存错误日志
     *
     * @param e 异常
     */
    public void error(Throwable e) {
        logService.error(e);
    }

    /**
     * 保存错误日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public void error(String spEl, String cause) {
        logService.error(spEl, cause);
    }

    /**
     * 保存调试日志
     *
     * @param spEl SpEl表达式
     */
    public void debug(@Language("spel") String spEl) {
        logService.debug(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     */
    public void warn(@Language("spel") String spEl) {
        logService.warn(spEl);
    }

    /**
     * 保存警告日志
     *
     * @param spEl SpEl表达式
     * @param e    异常
     */
    public void warn(@Language("spel") String spEl, Throwable e) {
        logService.warn(spEl, e);
    }

    /**
     * 保存警告日志
     *
     * @param e 异常
     */
    public void warn(Throwable e) {
        logService.warn(e);
    }

    /**
     * 保存警告日志
     *
     * @param spEl  SpEl表达式
     * @param cause 错误原因
     */
    public void warn(String spEl, String cause) {
        logService.warn(spEl, cause);
    }
}
