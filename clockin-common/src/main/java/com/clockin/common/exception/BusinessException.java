package com.clockin.common.exception;

import com.clockin.common.response.IResultCode;
import com.clockin.common.response.ResultCode;
import lombok.Getter;

/**
 * 業務異常
 */
@Getter
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(ResultCode.BUSINESS_ERROR.getCode(), message);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(IResultCode resultCode) {
        super(resultCode);
    }

    public BusinessException(IResultCode resultCode, Object data) {
        super(resultCode, data);
    }
}
