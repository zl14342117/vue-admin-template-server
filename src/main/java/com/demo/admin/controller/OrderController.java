package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.OrderCreateDTO;
import com.demo.admin.pojo.vo.OrderPageVO;
import com.demo.admin.pojo.vo.OrderVO;
import com.demo.admin.service.OrderService;
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

/**
 * 订单管理接口，对应前端 src/api/order.js。
 */
@RestController
@RequestMapping("/vue-admin-template/order")
public class OrderController {

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
