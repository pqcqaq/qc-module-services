package online.zust.qcqcqc.services.module.tasks.enums;

import lombok.Getter;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午9:22
 */
@Getter
public enum TaskType {
    /**
     * 任务类型
     */
    FETCH("Api调用"),
    SPEL("Spel表达式"),
    SHELL("Shell脚本"),
    PYTHON("Python脚本"),
    JAVASCRIPT("JavaScript脚本"),
    GROOVY("Groovy脚本");

    private final String type;

    TaskType(String type) {
        this.type = type;
    }

}
