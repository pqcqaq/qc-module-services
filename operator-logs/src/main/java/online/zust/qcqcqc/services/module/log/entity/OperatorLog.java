package online.zust.qcqcqc.services.module.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;
import online.zust.qcqcqc.utils.entity.BaseEntity;
import online.zust.qcqcqc.utils.generators.annotation.ColumnType;
import online.zust.qcqcqc.utils.generators.enums.DataType;

import java.io.Serial;

/**
 * @author qcqcqc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "operator_log")
public class OperatorLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8008114011853263091L;
    @ColumnType(type = DataType.Varchar, length = 31, comment = "日志级别", defaultValue = "'INFO'", nullable = false)
    private LogLevel level;
    @ColumnType(type = DataType.Varchar, length = 1023, comment = "日志信息", nullable = false)
    private String msg;
    @ColumnType(type = DataType.Tinyint, length = 1, comment = "是否成功", nullable = false)
    private Boolean success;
    @ColumnType(type = DataType.Varchar, length = 1023, comment = "失败原因")
    private String cause;
    @ColumnType(type = DataType.Varchar, length = 2047, comment = "元数据", nullable = false)
    private String metadata;
}
