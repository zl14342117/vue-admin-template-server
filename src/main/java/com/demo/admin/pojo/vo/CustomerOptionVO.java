package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户下拉选项。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOptionVO {

    private Long id;

    private String label;

    private String customerNo;
}
