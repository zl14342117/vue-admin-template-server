package com.demo.admin.pojo.vo;

import lombok.Data;

/**
 * 客户列表/详情出参。
 */
@Data
public class CustomerVO {

    private Long id;

    private String customerNo;

    private String name;

    private String contact;

    private String phone;

    private Integer status;

    private String createdAt;
}
