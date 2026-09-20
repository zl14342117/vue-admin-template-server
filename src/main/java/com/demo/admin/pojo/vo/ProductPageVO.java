package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品分页出参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageVO {

    private Long total;

    private List<ProductVO> list;
}
