package com.demo.admin.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 订单导出行。
 */
@Data
public class OrderExportRow {

    @ExcelProperty("订单号")
    @ColumnWidth(22)
    private String orderNo;

    @ExcelProperty("客户")
    @ColumnWidth(18)
    private String customerName;

    @ExcelProperty("金额")
    @ColumnWidth(12)
    private String totalAmount;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String statusText;

    @ExcelProperty("下单时间")
    @ColumnWidth(20)
    private String createdAt;
}
