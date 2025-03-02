package com.clockin.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 統一響應結構
 *
 * @param <T> 響應數據類型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * 響應代碼，0 表示成功
     */
    private int code;

    /**
     * 響應消息
     */
    private String message;

    /**
     * 響應數據
     */
    private T data;

    /**
     * 創建成功響應
     *
     * @param <T> 數據類型
     * @return 成功響應
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(0)
                .message("操作成功")
                .build();
    }

    /**
     * 創建成功響應，帶數據
     *
     * @param data 響應數據
     * @param <T>  數據類型
     * @return 成功響應
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(0)
                .message("操作成功")
                .data(data)
                .build();
    }

    /**
     * 創建成功響應，帶消息和數據
     *
     * @param message 響應消息
     * @param data    響應數據
     * @param <T>     數據類型
     * @return 成功響應
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code(0)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 創建錯誤響應，只帶錯誤消息
     *
     * @param message 錯誤消息
     * @param <T>     數據類型
     * @return 錯誤響應
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .code(400)
                .message(message)
                .build();
    }

    /**
     * 創建錯誤響應
     *
     * @param code    錯誤代碼
     * @param message 錯誤消息
     * @param <T>     數據類型
     * @return 錯誤響應
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    /**
     * 創建錯誤響應，帶數據
     *
     * @param code    錯誤代碼
     * @param message 錯誤消息
     * @param data    錯誤數據
     * @param <T>     數據類型
     * @return 錯誤響應
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
