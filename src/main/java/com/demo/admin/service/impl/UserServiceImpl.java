package com.demo.admin.service.impl;

import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.pojo.dto.LoginRequest;
import com.demo.admin.pojo.vo.TokenVO;
import com.demo.admin.pojo.vo.UserInfoVO;
import com.demo.admin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户业务实现，使用内存数据模拟登录态（与前端 mock 账号一致）。
 */
@Service
public class UserServiceImpl implements UserService {

    /** 用户名 -> token 映射 */
    private static final Map<String, String> USER_TOKENS = new HashMap<String, String>();

    /** token -> 用户信息映射 */
    private static final Map<String, UserInfoVO> TOKEN_USERS = new HashMap<String, UserInfoVO>();

    static {
        USER_TOKENS.put("admin", "admin-token");
        USER_TOKENS.put("editor", "editor-token");

        TOKEN_USERS.put("admin-token", new UserInfoVO(
                Arrays.asList("admin"),
                "I am a super administrator",
                "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
                "Super Admin"
        ));
        TOKEN_USERS.put("editor-token", new UserInfoVO(
                Arrays.asList("editor"),
                "I am an editor",
                "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
                "Normal Editor"
        ));
    }

    @Override
    public TokenVO login(LoginRequest request) {
        String token = USER_TOKENS.get(request.getUsername());
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ApiCodes.LOGIN_FAILED, "账号或密码错误");
        }
        return new TokenVO(token);
    }

    @Override
    public UserInfoVO getInfo(String token) {
        UserInfoVO userInfo = TOKEN_USERS.get(token);
        if (userInfo == null) {
            throw new BusinessException(ApiCodes.INVALID_TOKEN, "登录失效，无法获取用户信息");
        }
        return userInfo;
    }

    @Override
    public void logout() {
        // Demo 项目使用无状态 token，登出逻辑由前端清除 cookie 完成
    }
}
