package com.demo.admin.common;

import lombok.Data;

/**
 * 统一 API 响应结构，与前端 request.js 约定的 code / message / data 保持一致。
 */
@Data
public class ApiResponse<T> {

    /** 业务状态码，20000 表示成功 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(20000, "success", data);
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
