package com.demo.admin.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 工作台汇总出参。
 */
@Data
public class DashboardSummaryVO {

    /** 客户数（客户模块未建前为演示数据） */
    private Long customerCount;

    /** 商品数（商品模块未建前为演示数据） */
    private Long productCount;

    /** 今日订单数 */
    private Long todayOrderCount;

    /** 今日销售额 */
    private BigDecimal todaySalesAmount;

    /** 系统用户数（真实查库） */
    private Long userCount;

    /** 近 7 日订单趋势 */
    private List<DashboardTrendItemVO> trend;
}
