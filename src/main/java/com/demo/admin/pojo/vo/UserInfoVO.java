package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户详情，字段与前端 mock/user.js 保持一致。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    /** 角色列表 */
    private List<String> roles;

    /** 个人简介 */
    private String introduction;

    /** 头像地址 */
    private String avatar;

    /** 显示名称 */
    private String name;
}
