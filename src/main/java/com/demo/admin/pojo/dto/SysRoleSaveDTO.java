package com.demo.admin.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 系统角色新增/编辑入参。
 */
@Data
public class SysRoleSaveDTO {

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotBlank(message = "角色标识不能为空")
    @Pattern(regexp = "^[a-z][a-z0-9_]{1,31}$", message = "角色标识需小写字母开头，仅含小写字母/数字/下划线")
    private String code;

    private String description;
}
