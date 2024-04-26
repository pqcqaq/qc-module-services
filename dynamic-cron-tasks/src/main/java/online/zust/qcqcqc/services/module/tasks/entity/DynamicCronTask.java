package online.zust.qcqcqc.services.module.tasks.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.zust.qcqcqc.services.module.tasks.enums.TaskStatus;
import online.zust.qcqcqc.services.module.tasks.enums.TaskType;
import online.zust.qcqcqc.utils.entity.BaseEntity;
import online.zust.qcqcqc.utils.generators.annotation.ColumnType;
import online.zust.qcqcqc.utils.generators.enums.DataType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dynamic_cron_task")
public class DynamicCronTask extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -8831501150222479599L;
    @ColumnType(type = DataType.Varchar, comment = "任务名称", nullable = false)
    private String taskName;
    @ColumnType(type = DataType.Varchar, comment = "任务时间表达式", nullable = false)
    private String cronExpression;
    @ColumnType(type = DataType.Varchar, comment = "任务描述")
    private String taskExplain;
    @ColumnType(type = DataType.Tinyint, comment = "是否跟随服务启动", nullable = false, defaultValue = "0")
    private Boolean onBoot;
    @ColumnType(type = DataType.Varchar, comment = "任务类型", nullable = false, defaultValue = "'SPEL'")
    private TaskType taskType;
    @ColumnType(type = DataType.Varchar, comment = "任务状态", nullable = false, defaultValue = "'NORMAL'")
    private TaskStatus status;
    @ColumnType(type = DataType.Tinyint, comment = "上一次是否执行成功")
    private Boolean isLastSuccess;
    @ColumnType(type = DataType.Json, comment = "任务执行元数据", nullable = false)
    private String metadata;
}
