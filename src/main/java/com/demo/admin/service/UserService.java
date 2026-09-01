package com.demo.admin.service;

import com.demo.admin.pojo.dto.LoginRequest;
import com.demo.admin.pojo.vo.TokenVO;
import com.demo.admin.pojo.vo.UserInfoVO;

/**
 * 用户业务接口。
 */
public interface UserService {

    /** 登录并返回 token */
    TokenVO login(LoginRequest request);

    /** 根据 token 查询用户信息 */
    UserInfoVO getInfo(String token);

    /** 登出 */
    void logout();
}
