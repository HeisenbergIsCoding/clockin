package com.clockin.common.exception;

import com.clockin.common.response.ResultCode;

/**
 * 權限異常
 */
public class PermissionException extends BaseException {

    private static final long serialVersionUID = 1L;

    public PermissionException(String message) {
        super(ResultCode.PERMISSION_DENIED.getCode(), message);
    }
}
