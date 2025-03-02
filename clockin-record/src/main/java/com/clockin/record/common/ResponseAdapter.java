package com.clockin.record.common;

import com.clockin.record.dto.ApiResponse;

/**
 * 響應適配器
 * <p>
 * 提供在 R 和 ApiResponse 之間轉換的工具方法
 */
public class ResponseAdapter {

    private ResponseAdapter() {
        throw new IllegalStateException("工具類不能實例化");
    }

    /**
     * 將 R 轉換為 ApiResponse
     *
     * @param r R實例
     * @param <T> 數據類型
     * @return ApiResponse實例
     */
    public static <T> ApiResponse<T> toApiResponse(R<T> r) {
        if (r.isSuccess()) {
            return ApiResponse.<T>builder()
                    .code(r.getCode())
                    .message(r.getMessage())
                    .data(r.getData())
                    .build();
        } else {
            return ApiResponse.<T>builder()
                    .code(r.getCode())
                    .message(r.getMessage())
                    .build();
        }
    }

    /**
     * 將 ApiResponse 轉換為 R
     *
     * @param apiResponse ApiResponse實例
     * @param <T> 數據類型
     * @return R實例
     */
    public static <T> R<T> toR(ApiResponse<T> apiResponse) {
        R<T> r = new R<>();
        r.setCode(apiResponse.getCode());
        r.setMessage(apiResponse.getMessage());
        r.setData(apiResponse.getData());
        r.setSuccess(apiResponse.getCode() == 0 || apiResponse.getCode() == 200);
        return r;
    }
}
