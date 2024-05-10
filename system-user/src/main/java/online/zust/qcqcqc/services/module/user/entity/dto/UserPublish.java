package online.zust.qcqcqc.services.module.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author qcqcqc
 * Date: 2024/3/19
 * Time: 23:47
 */
@Data
public class UserPublish {
    private String id;
    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度必须在4-20之间")
    private String username;
    private String password;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    private Long avatarId;
    private String role;
}
