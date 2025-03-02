package com.clockin.common.exception;

import com.clockin.common.response.ResultCode;

/**
 * 資源不存在異常
 */
public class ResourceNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(ResultCode.NOT_FOUND.getCode(), message);
    }
}
