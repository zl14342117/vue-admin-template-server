package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.LoginRequest;
import com.demo.admin.pojo.vo.TokenVO;
import com.demo.admin.pojo.vo.UserInfoVO;
import com.demo.admin.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户相关接口，对应前端 src/api/user.js。
 */
@RestController
@RequestMapping("/vue-admin-template/user")
public class UserController {

    @Resource
    private UserService userService;

    /** 用户登录 */
    @PostMapping("/login")
    public ApiResponse<TokenVO> login(@RequestBody @Validated LoginRequest request) {
        return ApiResponse.success(userService.login(request));
    }

    /** 根据 token 获取当前用户信息 */
    @GetMapping("/info")
    public ApiResponse<UserInfoVO> info(@RequestParam("token") String token) {
        return ApiResponse.success(userService.getInfo(token));
    }

    /** 用户登出 */
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        userService.logout();
        return ApiResponse.success("success");
    }
}
