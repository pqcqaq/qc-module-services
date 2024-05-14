package online.zust.qcqcqc.services.module.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pqcmm
 */
@Data
public class RegisterParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 9195822302305777830L;
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 20, message = "用户名长度为1-20位")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 30, message = "密码长度为8-30位")
    private String password;
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度为11位")
    private String phone;
}
