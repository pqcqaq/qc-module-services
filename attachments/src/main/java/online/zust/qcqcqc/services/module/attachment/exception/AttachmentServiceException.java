package online.zust.qcqcqc.services.module.attachment.exception;


import online.zust.qcqcqc.services.exception.ServiceException;

import java.io.Serial;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 23:14
 */
public class AttachmentServiceException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 3081636071240919465L;

    public AttachmentServiceException(String message) {
        super(message);
    }

    public AttachmentServiceException(int code, String message) {
        super(message, code);
    }
}
