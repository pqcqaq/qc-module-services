package online.zust.qcqcqc.services.module.user.exception;

import lombok.Getter;
import online.zust.qcqcqc.services.exception.ServiceException;

import java.io.Serial;

/**
 * @author qcqcqc
 * Date: 2024/5/10
 * Time: 下午11:59
 */
@Getter
public class ErrorAlterUserException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 1304833249607014816L;

    public ErrorAlterUserException(String message) {
        super(message);
    }

    public ErrorAlterUserException(String message, int code) {
        super(message, code);
    }
}
