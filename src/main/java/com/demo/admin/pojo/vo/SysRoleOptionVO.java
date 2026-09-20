package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色下拉选项。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleOptionVO {

    private String label;

    private String value;
}
