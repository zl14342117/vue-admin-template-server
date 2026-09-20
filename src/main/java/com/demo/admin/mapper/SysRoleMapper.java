package com.demo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.admin.pojo.entity.SysRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统角色 Mapper。
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {
}
