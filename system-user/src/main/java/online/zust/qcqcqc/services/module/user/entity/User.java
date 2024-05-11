package online.zust.qcqcqc.services.module.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import online.zust.qcqcqc.services.module.attachment.entity.Attachment;
import online.zust.qcqcqc.services.module.attachment.service.impl.AttachmentsServiceImpl;
import online.zust.qcqcqc.utils.annotation.MsgOnCheckError;
import online.zust.qcqcqc.utils.annotation.OtODeepSearch;
import online.zust.qcqcqc.utils.entity.BaseEntity;
import online.zust.qcqcqc.utils.generators.annotation.ColumnType;
import online.zust.qcqcqc.utils.generators.enums.DataType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * @author qcqcqc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "user", autoResultMap = true)
public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5497104497125454445L;

    @ColumnType(type = DataType.Varchar, comment = "用户名", nullable = false, length = 63)
    private String username;
    @ColumnType(type = DataType.Varchar, comment = "密码", nullable = false, length = 127)
    private String password;
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnType(type = DataType.Json, comment = "角色", nullable = false)
    private List<String> roles;
    @ColumnType(type = DataType.Varchar, comment = "手机号", length = 63)
    private String phone;
    @ColumnType(type = DataType.Bigint, comment = "头像图片ID")
    private Long avatarId;
    @ColumnType(type = DataType.Tinyint, length = 1, comment = "是否启用", nullable = false, defaultValue = "1")
    private boolean enabled;

    /**
     * 查询关联的图片
     */
    @TableField(exist = false)
    @OtODeepSearch(baseId = "avatarId", service = AttachmentsServiceImpl.class)
    @MsgOnCheckError("用户头像正在使用该照片")
    private Attachment imageInfo;
}
