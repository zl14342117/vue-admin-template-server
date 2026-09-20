package com.demo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.admin.pojo.entity.OrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {
}
