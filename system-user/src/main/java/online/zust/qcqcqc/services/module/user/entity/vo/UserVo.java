package online.zust.qcqcqc.services.module.user.entity.vo;

import lombok.Data;
import online.zust.qcqcqc.utils.annotation.convert.CustomConvert;
import online.zust.qcqcqc.utils.annotation.convert.FromField;
import online.zust.qcqcqc.utils.annotation.convert.RequireDefault;

import java.util.Date;
import java.util.List;

/**
 * @author qcqcqc
 */
@Data
@CustomConvert
public class UserVo {
    private Long id;
    private String username;
    @FromField(fieldPath = "imageInfo.url")
    private String avatar;
    private String phone;
    @FromField(fieldPath = "roles")
    private List<String> roles;
    private Boolean enabled;
    @RequireDefault("\"******\"")
    private String password;
    private Date createTime;
    private String token;
}
