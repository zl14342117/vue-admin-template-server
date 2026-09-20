package com.demo.admin.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单列表/详情出参。
 */
@Data
public class OrderVO {

    private Long id;

    private String orderNo;

    private Long customerId;

    private String customerName;

    private BigDecimal totalAmount;

    private Integer status;

    private String createdAt;

    private List<OrderItemVO> items;
}
