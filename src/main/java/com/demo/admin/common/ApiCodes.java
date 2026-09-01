package com.demo.admin.common;

/**
 * 业务状态码常量，与 vue-admin-template 前端 mock 保持一致。
 */
public final class ApiCodes {

    /** 请求成功 */
    public static final int SUCCESS = 20000;

    /** 账号或密码错误 */
    public static final int LOGIN_FAILED = 60204;

    /** token 无效 */
    public static final int INVALID_TOKEN = 50008;

    private ApiCodes() {
    }
}
