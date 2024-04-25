package online.zust.qcqcqc.services.module.tasks.enums;

import lombok.Getter;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:50
 */
@Getter
public enum TaskStatus {
    /**
     * 任务状态
     */
    NORMAL("正常"),
    RUNNING("运行中"),
    PAUSE("暂停");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

}
