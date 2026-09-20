package com.demo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细实体，对应表 sys_order_item。
 */
@Data
@TableName("sys_order_item")
public class OrderItemDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private String productCode;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal amount;
}
