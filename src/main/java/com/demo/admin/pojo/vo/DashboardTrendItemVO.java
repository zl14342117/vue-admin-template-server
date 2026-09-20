package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作台近 N 日趋势点。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTrendItemVO {

    /** 日期，格式 yyyy-MM-dd */
    private String date;

    /** 当日订单数 */
    private Integer count;
}
