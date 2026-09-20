package com.demo.admin.pojo.vo;

import lombok.Data;

/**
 * 系统用户列表/详情出参。
 */
@Data
public class SysUserVO {

    private Long id;

    private String username;

    private String nickname;

    private String role;

    private Integer status;

    private String createdAt;
}
