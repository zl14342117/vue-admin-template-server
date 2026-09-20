package com.demo.admin.service;

import com.demo.admin.pojo.dto.SysRoleSaveDTO;
import com.demo.admin.pojo.vo.SysRoleOptionVO;
import com.demo.admin.pojo.vo.SysRolePageVO;
import com.demo.admin.pojo.vo.SysRoleVO;

import java.util.List;

/**
 * 系统角色管理业务接口。
 */
public interface SysRoleService {

    SysRolePageVO list(Integer page, Integer limit, String keyword);

    List<SysRoleOptionVO> options();

    SysRoleVO detail(Long id);

    SysRoleVO create(SysRoleSaveDTO dto);

    SysRoleVO update(Long id, SysRoleSaveDTO dto);

    void delete(Long id);
}
