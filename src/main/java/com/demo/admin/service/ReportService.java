package com.demo.admin.service;

import com.demo.admin.pojo.vo.ReportSalesVO;

/**
 * 销售报表。
 */
public interface ReportService {

    /**
     * 按日期范围聚合已支付订单的每日销售额与分类占比。
     *
     * @param startDate 起始日 yyyy-MM-dd，可空（默认今天往前 29 天）
     * @param endDate   结束日 yyyy-MM-dd，可空（默认今天）
     */
    ReportSalesVO sales(String startDate, String endDate);
}
