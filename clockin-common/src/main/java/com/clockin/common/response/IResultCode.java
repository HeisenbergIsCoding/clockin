package com.clockin.common.response;

/**
 * 結果碼接口
 */
public interface IResultCode {
    
    /**
     * 獲取狀態碼
     *
     * @return 狀態碼
     */
    Integer getCode();
    
    /**
     * 獲取消息
     *
     * @return 消息
     */
    String getMessage();
}
