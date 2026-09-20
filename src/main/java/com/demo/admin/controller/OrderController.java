package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.OrderCreateDTO;
import com.demo.admin.pojo.excel.OrderExportRow;
import com.demo.admin.pojo.vo.OrderPageVO;
import com.demo.admin.pojo.vo.OrderVO;
import com.demo.admin.service.OrderService;
import com.demo.admin.util.ExcelExportUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单管理接口，对应前端 src/api/order.js。
 */
@RestController
@RequestMapping("/vue-admin-template/order")
public class OrderController {

    private static final DateTimeFormatter FILE_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public ApiResponse<OrderPageVO> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {
        return ApiResponse.success(orderService.list(page, limit, keyword, status));
    }

    /** 导出订单 Excel（按当前筛选） */
    @GetMapping("/export")
    public void export(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status,
            HttpServletResponse response) throws IOException {
        List<OrderExportRow> rows = orderService.listForExport(keyword, status);
        String filename = "订单列表_" + LocalDate.now().format(FILE_DAY) + ".xlsx";
        ExcelExportUtil.write(response, filename, OrderExportRow.class, rows);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderVO> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(orderService.detail(id));
    }

    @PostMapping
    public ApiResponse<OrderVO> create(@RequestBody @Validated OrderCreateDTO dto) {
        return ApiResponse.success(orderService.create(dto));
    }

    @PutMapping("/{id}/pay")
    public ApiResponse<OrderVO> pay(@PathVariable("id") Long id) {
        return ApiResponse.success(orderService.pay(id));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderVO> cancel(@PathVariable("id") Long id) {
        return ApiResponse.success(orderService.cancel(id));
    }
}
