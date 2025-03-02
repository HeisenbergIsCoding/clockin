package com.clockin.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 統一API響應結果
 *
 * @param <T> 數據類型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 狀態碼
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 數據
     */
    private T data;

    /**
     * 請求標識符
     */
    private String requestId;

    /**
     * 時間戳
     */
    private long timestamp;

    /**
     * 構造方法
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 構造方法
     *
     * @param code    狀態碼
     * @param message 消息
     * @param data    數據
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 返回成功結果
     *
     * @param <T> 數據類型
     * @return 返回結果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 返回成功結果
     *
     * @param data 數據
     * @param <T>  數據類型
     * @return 返回結果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功結果
     *
     * @param data    數據
     * @param message 消息
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回錯誤結果
     *
     * @param <T> 數據類型
     * @return 返回結果
     */
    public static <T> Result<T> error() {
        return error(ResultCode.ERROR);
    }

    /**
     * 返回錯誤結果
     *
     * @param resultCode 結果碼
     * @param <T>        數據類型
     * @return 返回結果
     */
    public static <T> Result<T> error(IResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 返回錯誤結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.ERROR.getCode(), message, null);
    }

    /**
     * 返回錯誤結果
     *
     * @param code    狀態碼
     * @param message 消息
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 判斷是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
