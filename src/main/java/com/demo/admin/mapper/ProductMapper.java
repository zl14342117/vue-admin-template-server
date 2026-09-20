package com.demo.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.admin.pojo.entity.ProductDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品 Mapper。
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductDO> {
}
