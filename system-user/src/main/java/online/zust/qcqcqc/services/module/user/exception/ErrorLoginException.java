package online.zust.qcqcqc.services.module.user.exception;

import lombok.Getter;

import java.io.Serial;
import java.rmi.ServerException;

/**
 * @author qcqcqc
 * Date: 2024/5/10
 * Time: 下午11:46
 */
@Getter
public class ErrorLoginException extends ServerException {
    @Serial
    private static final long serialVersionUID = 7503551487558248481L;

    public ErrorLoginException(String s) {
        super(s);
    }

    public ErrorLoginException(String s, Exception ex) {
        super(s, ex);
    }
}
