package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.CustomerSaveDTO;
import com.demo.admin.pojo.excel.CustomerExportRow;
import com.demo.admin.pojo.vo.CustomerOptionVO;
import com.demo.admin.pojo.vo.CustomerPageVO;
import com.demo.admin.pojo.vo.CustomerVO;
import com.demo.admin.service.CustomerService;
import com.demo.admin.util.ExcelExportUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * 客户管理接口，对应前端 src/api/customer.js。
 */
@RestController
@RequestMapping("/vue-admin-template/customer")
public class CustomerController {

    private static final DateTimeFormatter FILE_DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private CustomerService customerService;

    /** 分页查询客户列表 */
    @GetMapping("/list")
    public ApiResponse<CustomerPageVO> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {
        return ApiResponse.success(customerService.list(page, limit, keyword, status));
    }

    /** 导出客户 Excel（按当前筛选） */
    @GetMapping("/export")
    public void export(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status,
            HttpServletResponse response) throws IOException {
        List<CustomerExportRow> rows = customerService.listForExport(keyword, status);
        String filename = "客户列表_" + LocalDate.now().format(FILE_DAY) + ".xlsx";
        ExcelExportUtil.write(response, filename, CustomerExportRow.class, rows);
    }

    /** 启用客户下拉（订单模块使用） */
    @GetMapping("/options")
    public ApiResponse<List<CustomerOptionVO>> options() {
        return ApiResponse.success(customerService.options());
    }

    /** 客户详情 */
    @GetMapping("/{id}")
    public ApiResponse<CustomerVO> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(customerService.detail(id));
    }

    /** 新增客户 */
    @PostMapping
    public ApiResponse<CustomerVO> create(@RequestBody @Validated CustomerSaveDTO dto) {
        return ApiResponse.success(customerService.create(dto));
    }

    /** 编辑客户 */
    @PutMapping("/{id}")
    public ApiResponse<CustomerVO> update(@PathVariable("id") Long id,
                                          @RequestBody @Validated CustomerSaveDTO dto) {
        return ApiResponse.success(customerService.update(id, dto));
    }

    /** 删除客户 */
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return ApiResponse.success("success");
    }
}
