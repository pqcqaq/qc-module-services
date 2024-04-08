package online.zust.qcqcqc.services.module.log.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OperatorLog {
    private Long id;
    private String msg;
    private Date createTime;
    private Long createBy;
    private Boolean success;
    private String cause;
}
