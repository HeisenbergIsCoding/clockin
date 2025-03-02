package com.clockin.record.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 統一響應結果封裝類
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功標記
     */
    private boolean success;

    /**
     * 返回代碼
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回數據
     */
    private T data;

    /**
     * 成功返回結果
     *
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回結果
     *
     * @param data 返回的數據
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> ok(T data) {
        R<T> result = new R<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功返回結果，帶自定義消息
     *
     * @param data 返回的數據
     * @param message 成功消息
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> ok(T data, String message) {
        R<T> result = new R<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功返回結果（與ok方法相同，用於保持API一致性）
     *
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> success() {
        return ok(null);
    }

    /**
     * 成功返回結果（與ok方法相同，用於保持API一致性）
     *
     * @param data 返回的數據
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> success(T data) {
        return ok(data);
    }
    
    /**
     * 成功返回結果，帶自定義消息
     *
     * @param data 返回的數據
     * @param message 成功消息
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> success(T data, String message) {
        return ok(data, message);
    }

    /**
     * 失敗返回結果
     *
     * @param message 錯誤消息
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> fail(String message) {
        return fail(ResultCode.FAILURE, message);
    }

    /**
     * 失敗返回結果
     *
     * @param code 錯誤代碼
     * @param message 錯誤消息
     * @param <T> 數據類型
     * @return 統一返回結果
     */
    public static <T> R<T> fail(Integer code, String message) {
        R<T> result = new R<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 檢查接口返回結果是否成功
     *
     * @param result 統一返回結果
     * @param <T> 數據類型
     * @return 是否成功
     */
    public static <T> boolean isSuccess(R<T> result) {
        return result != null && result.isSuccess();
    }

    /**
     * 獲取接口返回的數據
     *
     * @param result 統一返回結果
     * @param <T> 數據類型
     * @return 返回的數據
     */
    public static <T> T getData(R<T> result) {
        return result != null ? result.getData() : null;
    }
}
