package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 客户分页出参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPageVO {

    private Long total;

    private List<CustomerVO> list;
}
