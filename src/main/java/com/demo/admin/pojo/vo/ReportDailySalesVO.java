package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报表：每日销售额。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDailySalesVO {

    private String date;

    private BigDecimal amount;
}
