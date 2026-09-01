package com.demo.admin.service;

import com.demo.admin.pojo.vo.TableListVO;

/**
 * 表格业务接口。
 */
public interface TableService {

    /** 查询表格列表 */
    TableListVO list();
}
