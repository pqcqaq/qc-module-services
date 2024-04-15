package online.zust.qcqcqc.services.module.attachment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.zust.qcqcqc.utils.entity.BaseEntity;
import online.zust.qcqcqc.utils.generators.annotation.ColumnType;
import online.zust.qcqcqc.utils.generators.enums.DataType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pqcmm
 */
@EqualsAndHashCode(callSuper = true)
@TableName("attachment")
@Data
public class Attachment extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7519503360098918409L;
    @ColumnType(type = DataType.Varchar, comment = "文件名", nullable = false)
    private String originalFilename;
    @ColumnType(type = DataType.Varchar, comment = "文件路径", nullable = false)
    private String url;
    @ColumnType(type = DataType.Varchar, length = 32, comment = "文件md5", nullable = false)
    private String md5;
    @ColumnType(type = DataType.Varchar, length = 31, comment = "文件类型", nullable = false)
    private String fileType;
    @ColumnType(type = DataType.Varchar, length = 127, comment = "文件标签", nullable = false)
    private String tag;
}
