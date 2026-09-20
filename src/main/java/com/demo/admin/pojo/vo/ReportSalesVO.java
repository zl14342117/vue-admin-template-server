package com.demo.admin.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 销售报表出参。
 */
@Data
public class ReportSalesVO {

    /** 每日销售额（柱状图） */
    private List<ReportDailySalesVO> dailySales;

    /** 商品分类销售占比（饼图） */
    private List<ReportCategorySalesVO> categorySales;
}
