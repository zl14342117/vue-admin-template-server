package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表格列表响应体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableListVO {

    /** 总条数 */
    private Integer total;

    /** 数据列表 */
    private List<TableItemVO> items;
}
