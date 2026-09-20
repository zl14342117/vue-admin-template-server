package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报表：商品分类销售占比。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCategorySalesVO {

    private String category;

    private BigDecimal amount;
}
