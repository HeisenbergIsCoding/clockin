package com.clockin.record.exception;

import com.clockin.record.common.ResultCode;
import lombok.Getter;

/**
 * API業務異常
 */
@Getter
public class ApiException extends RuntimeException {

    private final Integer code;

    /**
     * 使用默認錯誤碼構造異常
     *
     * @param message 錯誤消息
     */
    public ApiException(String message) {
        this(ResultCode.BIZ_ERROR, message);
    }

    /**
     * 使用指定錯誤碼構造異常
     *
     * @param code 錯誤碼
     * @param message 錯誤消息
     */
    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用指定錯誤碼和原始異常構造異常
     *
     * @param code 錯誤碼
     * @param message 錯誤消息
     * @param cause 原始異常
     */
    public ApiException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
