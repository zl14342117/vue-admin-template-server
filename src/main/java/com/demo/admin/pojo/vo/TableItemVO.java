package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表格单行数据，序列化后字段名为 display_time（由 Jackson 蛇形命名策略转换）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableItemVO {

    private String id;
    private String title;
    private String status;
    private String author;
    private String displayTime;
    private Integer pageviews;
}
