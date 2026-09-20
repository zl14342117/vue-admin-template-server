package com.demo.admin.service.impl;

import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.pojo.dto.LoginRequest;
import com.demo.admin.pojo.entity.SysUserDO;
import com.demo.admin.pojo.vo.TokenVO;
import com.demo.admin.pojo.vo.UserInfoVO;
import com.demo.admin.service.SysUserService;
import com.demo.admin.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 登录鉴权业务实现，用户数据与系统用户管理共用。
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR =
            "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";

    @Resource
    private SysUserService sysUserService;

    @Override
    public TokenVO login(LoginRequest request) {
        SysUserDO user = sysUserService.findActiveUser(
                request.getUsername().trim(),
                request.getPassword()
        );
        if (user == null) {
            throw new BusinessException(ApiCodes.LOGIN_FAILED, "账号或密码错误");
        }
        return new TokenVO(SysUserServiceImpl.buildToken(user.getUsername()));
    }

    @Override
    public UserInfoVO getInfo(String token) {
        SysUserDO user = sysUserService.findByToken(token);
        if (user == null) {
            throw new BusinessException(ApiCodes.INVALID_TOKEN, "登录失效，无法获取用户信息");
        }
        return new UserInfoVO(
                Collections.singletonList(user.getRole()),
                "LiteOps 练手用户",
                DEFAULT_AVATAR,
                user.getNickname()
        );
    }

    @Override
    public void logout() {
        // 无状态 token，登出由前端清除 cookie 完成
    }
}
