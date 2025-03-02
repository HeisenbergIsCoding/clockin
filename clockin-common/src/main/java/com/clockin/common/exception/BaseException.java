package com.clockin.common.exception;

import com.clockin.common.response.IResultCode;
import com.clockin.common.response.ResultCode;
import lombok.Getter;

/**
 * 基礎異常類
 */
@Getter
public class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 錯誤碼
     */
    private final Integer code;
    
    /**
     * 錯誤信息
     */
    private final String message;
    
    /**
     * 異常數據
     */
    private final Object data;
    
    public BaseException(String message) {
        this(ResultCode.ERROR.getCode(), message, null);
    }
    
    public BaseException(Integer code, String message) {
        this(code, message, null);
    }
    
    public BaseException(IResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage(), null);
    }
    
    public BaseException(IResultCode resultCode, Object data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }
    
    public BaseException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
