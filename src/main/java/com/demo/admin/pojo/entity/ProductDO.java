package com.demo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表实体，对应表 sys_product。
 */
@Data
@TableName("sys_product")
public class ProductDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    /** digital / daily / food */
    private String category;

    private BigDecimal price;

    private Integer stock;

    /** 1 上架，0 下架 */
    private Integer status;

    private LocalDateTime createdAt;
}
