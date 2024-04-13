package online.zust.qcqcqc.services.module.redis.exception;

import online.zust.qcqcqc.services.exception.ServiceException;

import java.io.Serial;

/**
 * @author qcqcqc
 * Date: 2024/4/13
 * Time: 19:19
 */
public class RedisException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 5483370714995623677L;

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, int code) {
        super(message, code);
    }
}
