package com.demo.admin.service;

import com.demo.admin.pojo.dto.CustomerSaveDTO;
import com.demo.admin.pojo.excel.CustomerExportRow;
import com.demo.admin.pojo.vo.CustomerOptionVO;
import com.demo.admin.pojo.vo.CustomerPageVO;
import com.demo.admin.pojo.vo.CustomerVO;

import java.util.List;

/**
 * 客户管理业务接口。
 */
public interface CustomerService {

    CustomerPageVO list(Integer page, Integer limit, String keyword, Integer status);

    /** 按筛选条件导出（不分页） */
    List<CustomerExportRow> listForExport(String keyword, Integer status);

    List<CustomerOptionVO> options();

    CustomerVO detail(Long id);

    CustomerVO create(CustomerSaveDTO dto);

    CustomerVO update(Long id, CustomerSaveDTO dto);

    void delete(Long id);
}
