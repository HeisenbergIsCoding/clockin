package com.clockin.record.common;

/**
 * 統一返回狀態碼
 */
public interface ResultCode {

    /**
     * 操作成功
     */
    Integer SUCCESS = 200;

    /**
     * 操作失敗
     */
    Integer FAILURE = 500;

    /**
     * 參數錯誤
     */
    Integer PARAM_ERROR = 400;

    /**
     * 未授權
     */
    Integer UNAUTHORIZED = 401;

    /**
     * 禁止訪問
     */
    Integer FORBIDDEN = 403;

    /**
     * 資源不存在
     */
    Integer NOT_FOUND = 404;

    /**
     * 業務異常
     */
    Integer BIZ_ERROR = 600;
}
