package com.demo.admin.pojo.vo;

import lombok.Data;

/**
 * 系统角色列表/详情出参。
 */
@Data
public class SysRoleVO {

    private Long id;

    private String name;

    private String code;

    private String description;

    private String createdAt;
}
