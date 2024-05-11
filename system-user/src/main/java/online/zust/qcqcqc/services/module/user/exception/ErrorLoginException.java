package online.zust.qcqcqc.services.module.user.exception;

import lombok.Getter;
import online.zust.qcqcqc.services.exception.ServiceException;

import java.io.Serial;

/**
 * @author qcqcqc
 * Date: 2024/5/10
 * Time: 下午11:46
 */
@Getter
public class ErrorLoginException extends ServiceException {

    @Serial
    private static final long serialVersionUID = -3284770015633974298L;

    public ErrorLoginException(String message) {
        super(message);
    }

    public ErrorLoginException(String message, int code) {
        super(message, code);
    }
}
