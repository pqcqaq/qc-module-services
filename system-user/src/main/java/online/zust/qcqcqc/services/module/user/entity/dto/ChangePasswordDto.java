package online.zust.qcqcqc.services.module.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author qcqcqc
 * Date: 2024/5/11
 * Time: 上午12:38
 */
@Data
public class ChangePasswordDto {
    @NotBlank(message = "用户id不能为空")
    private String id;
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 30, message = "密码长度必须在6-30之间")
    private String password;
}
