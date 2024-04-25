package online.zust.qcqcqc.services.module.tasks.exception;

import online.zust.qcqcqc.services.exception.ServiceException;

import java.io.Serial;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午9:42
 */
public class DynamicTaskException extends ServiceException {
    @Serial
    private static final long serialVersionUID = -357571664576544990L;

    public DynamicTaskException(String message) {
        super(message);
    }

    public DynamicTaskException(String message, int code) {
        super(message, code);
    }
}
