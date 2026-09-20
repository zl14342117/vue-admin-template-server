package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 上架商品下拉选项（订单模块使用）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionVO {

    private Long id;

    private String label;

    private String code;

    private BigDecimal price;

    private Integer stock;
}
