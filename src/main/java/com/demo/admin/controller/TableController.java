package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.vo.TableListVO;
import com.demo.admin.service.TableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 表格数据接口，对应前端 src/api/table.js。
 */
@RestController
@RequestMapping("/vue-admin-template/table")
public class TableController {

    @Resource
    private TableService tableService;

    /** 查询表格列表 */
    @GetMapping("/list")
    public ApiResponse<TableListVO> list() {
        return ApiResponse.success(tableService.list());
    }
}
