package com.demo.admin.exception;

import lombok.Getter;

/**
 * 业务异常，携带自定义错误码，由全局异常处理器统一返回给前端。
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 业务错误码 */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
