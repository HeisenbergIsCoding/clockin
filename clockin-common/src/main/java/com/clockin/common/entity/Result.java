package com.clockin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用響應結果類
 *
 * @param <T> 數據類型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 成功標記
     */
    private boolean success;
    
    /**
     * 狀態碼
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 數據
     */
    private T data;
    
    /**
     * 返回成功結果
     *
     * @param <T> 數據類型
     * @return 結果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 返回成功結果
     *
     * @param data 數據
     * @param <T>  數據類型
     * @return 結果
     */
    public static <T> Result<T> success(T data) {
        return success(data, "操作成功");
    }
    
    /**
     * 返回成功結果
     *
     * @param data    數據
     * @param message 消息
     * @param <T>     數據類型
     * @return 結果
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, 200, message, data);
    }
    
    /**
     * 返回失敗結果
     *
     * @param <T> 數據類型
     * @return 結果
     */
    public static <T> Result<T> fail() {
        return fail("操作失敗");
    }
    
    /**
     * 返回失敗結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 結果
     */
    public static <T> Result<T> fail(String message) {
        return fail(message, 500);
    }
    
    /**
     * 返回失敗結果
     *
     * @param message 消息
     * @param code    狀態碼
     * @param <T>     數據類型
     * @return 結果
     */
    public static <T> Result<T> fail(String message, int code) {
        return new Result<>(false, code, message, null);
    }
    
    /**
     * 返回失敗結果
     *
     * @param message 消息
     * @param code    狀態碼
     * @param data    數據
     * @param <T>     數據類型
     * @return 結果
     */
    public static <T> Result<T> fail(String message, int code, T data) {
        return new Result<>(false, code, message, data);
    }
}
