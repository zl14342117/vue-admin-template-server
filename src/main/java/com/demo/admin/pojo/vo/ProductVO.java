package com.demo.admin.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品列表/详情出参。
 */
@Data
public class ProductVO {

    private Long id;

    private String name;

    private String code;

    private String category;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String imageUrl;

    private String createdAt;
}
