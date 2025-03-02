package com.clockin.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回結果
 */
@Data
public class ResponseResult<T> implements Serializable {

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
     * 是否成功
     */
    private Boolean success;

    /**
     * 時間戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 成功返回結果
     *
     * @param <T> 數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * 成功返回結果
     *
     * @param data 數據
     * @param <T>  數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功返回結果
     *
     * @param data    數據
     * @param message 消息
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(200);
        result.setData(data);
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    /**
     * 失敗返回結果
     *
     * @param <T> 數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> error() {
        return error("操作失敗");
    }

    /**
     * 失敗返回結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> error(String message) {
        return error(message, 500);
    }

    /**
     * 失敗返回結果
     *
     * @param message 消息
     * @param code    狀態碼
     * @param <T>     數據類型
     * @return 返回結果
     */
    public static <T> ResponseResult<T> error(String message, Integer code) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
