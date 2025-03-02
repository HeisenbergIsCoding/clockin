package com.clockin.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 標準結果碼
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 操作失敗
     */
    ERROR(500, "操作失敗"),
    
    /**
     * 參數錯誤
     */
    PARAM_ERROR(400, "參數錯誤"),
    
    /**
     * 未授權
     */
    UNAUTHORIZED(401, "未授權"),
    
    /**
     * 禁止訪問
     */
    FORBIDDEN(403, "禁止訪問"),
    
    /**
     * 資源不存在
     */
    NOT_FOUND(404, "資源不存在"),
    
    /**
     * 請求方法不允許
     */
    METHOD_NOT_ALLOWED(405, "請求方法不允許"),
    
    /**
     * 請求超時
     */
    REQUEST_TIMEOUT(408, "請求超時"),
    
    /**
     * 服務不可用
     */
    SERVICE_UNAVAILABLE(503, "服務不可用"),
    
    /**
     * 參數校驗失敗
     */
    VALIDATE_FAILED(1000, "參數校驗失敗"),
    
    /**
     * 請求頻繁
     */
    REQUEST_FREQUENT(1001, "請求頻繁，請稍後再試"),
    
    /**
     * 身份認證失敗
     */
    AUTHENTICATE_FAILED(1002, "身份認證失敗"),
    
    /**
     * 訪問權限不足
     */
    PERMISSION_DENIED(1003, "訪問權限不足"),
    
    /**
     * 業務處理失敗
     */
    BUSINESS_ERROR(1004, "業務處理失敗");
    
    /**
     * 狀態碼
     */
    private final Integer code;
    
    /**
     * 消息
     */
    private final String message;
}
