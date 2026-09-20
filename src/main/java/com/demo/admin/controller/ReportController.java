package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.vo.ReportSalesVO;
import com.demo.admin.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 销售报表接口，对应前端 src/api/report.js。
 */
@RestController
@RequestMapping("/vue-admin-template/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    /** 销售报表：每日销售额 + 分类占比 */
    @GetMapping("/sales")
    public ApiResponse<ReportSalesVO> sales(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return ApiResponse.success(reportService.sales(startDate, endDate));
    }
}
