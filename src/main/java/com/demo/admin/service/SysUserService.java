package com.demo.admin.service;

import com.demo.admin.pojo.dto.SysUserSaveDTO;
import com.demo.admin.pojo.entity.SysUserDO;
import com.demo.admin.pojo.vo.SysUserPageVO;
import com.demo.admin.pojo.vo.SysUserVO;

/**
 * 系统用户管理业务接口。
 */
public interface SysUserService {

    SysUserPageVO list(Integer page, Integer limit, String keyword);

    SysUserVO detail(Long id);

    SysUserVO create(SysUserSaveDTO dto);

    SysUserVO update(Long id, SysUserSaveDTO dto);

    void delete(Long id);

    /** 登录鉴权：按用户名密码查找启用用户 */
    SysUserDO findActiveUser(String username, String password);

    /** 登录鉴权：按 token 查找用户 */
    SysUserDO findByToken(String token);
}
