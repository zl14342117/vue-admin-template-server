package com.demo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.admin.pojo.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户 Mapper，继承 MyBatis-Plus BaseMapper 获得基础 CRUD。
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserDO> {
}
