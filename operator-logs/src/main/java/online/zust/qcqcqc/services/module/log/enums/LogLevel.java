package online.zust.qcqcqc.services.module.log.enums;

import lombok.Getter;

/**
 * @author qcqcqc
 * Date: 2024/4/8
 * Time: 22:53
 */
@Getter
public enum LogLevel {
    /**
     * 日志级别: INFO
     */
    INFO(1, "INFO"),
    /**
     * 日志级别: DEBUG
     */
    DEBUG(2, "DEBUG"),
    /**
     * 日志级别: WARN
     */
    WARN(3, "WARN"),
    /**
     * 日志级别: ERROR
     */
    ERROR(4, "ERROR");

    private final int level;
    private final String name;

    LogLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

}
