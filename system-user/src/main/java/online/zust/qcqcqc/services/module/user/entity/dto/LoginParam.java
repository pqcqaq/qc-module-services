package online.zust.qcqcqc.services.module.user.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author qcqcqc
 */
@Data
public class LoginParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 595071560371539305L;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 30, message = "密码长度为8-30位")
    private String password;
}
