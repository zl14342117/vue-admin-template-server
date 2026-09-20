package com.demo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表实体，对应表 sys_order。
 */
@Data
@TableName("sys_order")
public class OrderDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long customerId;

    private String customerName;

    private BigDecimal totalAmount;

    /** 0 待支付，1 已支付，2 已取消 */
    private Integer status;

    private LocalDateTime createdAt;
}
