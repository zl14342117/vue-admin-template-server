package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.vo.DashboardSummaryVO;
import com.demo.admin.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工作台接口，对应前端 src/api/dashboard.js。
 */
@RestController
@RequestMapping("/vue-admin-template/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /** 工作台汇总：统计卡片 + 近 7 日趋势 */
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryVO> summary() {
        return ApiResponse.success(dashboardService.summary());
    }
}
