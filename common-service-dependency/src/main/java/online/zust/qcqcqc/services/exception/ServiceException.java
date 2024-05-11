package online.zust.qcqcqc.services.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * @author qcqcqc
 * @date 2024/4/8
 * @time 10:29
 */
@Getter
@SuppressWarnings("unused")
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5189846607032477439L;
    private final int code;

    public ServiceException(String message) {
        super(message);
        this.code = 500;
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }
}
