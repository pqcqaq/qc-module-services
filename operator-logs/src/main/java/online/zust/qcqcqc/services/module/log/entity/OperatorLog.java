package online.zust.qcqcqc.services.module.log.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;

import java.util.Date;

/**
 * @author qcqcqc
 */
@Data
@TableName(value = "operator_log")
public class OperatorLog {
    @TableId
    private Long id;
    private LogLevel level;
    private String msg;
    private Date createTime;
    private Long createBy;
    private Boolean success;
    private String cause;
}
