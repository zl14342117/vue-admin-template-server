package com.demo.admin.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 客户导出行。
 */
@Data
public class CustomerExportRow {

    @ExcelProperty("客户编号")
    @ColumnWidth(20)
    private String customerNo;

    @ExcelProperty("客户名称")
    @ColumnWidth(18)
    private String name;

    @ExcelProperty("联系人")
    @ColumnWidth(12)
    private String contact;

    @ExcelProperty("手机号")
    @ColumnWidth(16)
    private String phone;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String statusText;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private String createdAt;
}
