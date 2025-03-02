package com.clockin.auth.common;

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
     * 時間戳
     */
    private long timestamp;

    /**
     * 默認構造函數
     */
    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 建立成功結果
     *
     * @param <T> 數據類型
     * @return 成功結果
     */
    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("操作成功");
        return r;
    }

    /**
     * 建立帶數據的成功結果
     *
     * @param data 數據
     * @param <T>  數據類型
     * @return 成功結果
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    /**
     * 建立帶消息的成功結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 成功結果
     */
    public static <T> R<T> ok(String message) {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(message);
        return r;
    }

    /**
     * 建立成功結果
     *
     * @param <T> 數據類型
     * @return 成功結果
     */
    public static <T> R<T> success() {
        return ok();
    }

    /**
     * 建立帶數據的成功結果
     *
     * @param data 數據
     * @param <T>  數據類型
     * @return 成功結果
     */
    public static <T> R<T> success(T data) {
        return ok(data);
    }

    /**
     * 建立帶消息的成功結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 成功結果
     */
    public static <T> R<T> success(String message) {
        return ok(message);
    }

    /**
     * 建立失敗結果
     *
     * @param <T> 數據類型
     * @return 失敗結果
     */
    public static <T> R<T> fail() {
        R<T> r = new R<>();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage("操作失敗");
        return r;
    }

    /**
     * 建立帶消息的失敗結果
     *
     * @param message 消息
     * @param <T>     數據類型
     * @return 失敗結果
     */
    public static <T> R<T> fail(String message) {
        R<T> r = new R<>();
        r.setSuccess(false);
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    /**
     * 建立帶代碼和消息的失敗結果
     *
     * @param code    代碼
     * @param message 消息
     * @param <T>     數據類型
     * @return 失敗結果
     */
    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<>();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
