package com.demo.admin.service;

import com.demo.admin.pojo.dto.OrderCreateDTO;
import com.demo.admin.pojo.excel.OrderExportRow;
import com.demo.admin.pojo.vo.OrderPageVO;
import com.demo.admin.pojo.vo.OrderVO;

import java.util.List;

/**
 * 订单管理业务接口。
 */
public interface OrderService {

    OrderPageVO list(Integer page, Integer limit, String keyword, Integer status);

    /** 按筛选条件导出（不分页） */
    List<OrderExportRow> listForExport(String keyword, Integer status);

    OrderVO detail(Long id);

    OrderVO create(OrderCreateDTO dto);

    OrderVO pay(Long id);

    OrderVO cancel(Long id);
}
