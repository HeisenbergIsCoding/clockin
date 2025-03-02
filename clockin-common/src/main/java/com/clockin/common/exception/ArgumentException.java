package com.clockin.common.exception;

import com.clockin.common.response.ResultCode;

/**
 * 參數驗證異常
 */
public class ArgumentException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ArgumentException(String message) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
    }
}
