package com.clockin.common.exception;

import com.clockin.common.response.ResultCode;

/**
 * 認證異常
 */
public class AuthException extends BaseException {

    private static final long serialVersionUID = 1L;

    public AuthException(String message) {
        super(ResultCode.AUTHENTICATE_FAILED.getCode(), message);
    }
}
