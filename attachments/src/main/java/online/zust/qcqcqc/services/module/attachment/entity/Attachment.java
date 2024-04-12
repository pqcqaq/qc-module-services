package online.zust.qcqcqc.services.module.attachment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author pqcmm
 */
@TableName("attachment")
@Data
public class Attachment implements Serializable {
    @Serial
    private static final long serialVersionUID = 7519503360098918409L;
    @TableId
    private Long id;
    private String originalFilename;
    private String url;
    private String md5;
    private String fileType;
    private String tag;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
