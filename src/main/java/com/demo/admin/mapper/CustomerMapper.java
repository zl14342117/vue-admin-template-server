package com.demo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.admin.pojo.entity.CustomerDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户 Mapper。
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerDO> {
}
