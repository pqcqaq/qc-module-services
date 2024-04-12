package online.zust.qcqcqc.services.entity.dto;

import lombok.Getter;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 8:18
 */
@Getter
public enum ResultCode {
    /**
     * 网络情况
     */
    SUCCESS(200, "Success"),
    ERROR(404, "Not Found"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    BANDED(402, "Banded"),
    GO_TO_LOGIN(302, "Go to Login"),
    REDIRECT(302, "Redirect");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
