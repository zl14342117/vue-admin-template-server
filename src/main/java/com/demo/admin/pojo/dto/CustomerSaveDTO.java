package com.demo.admin.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 客户新增/编辑入参。
 */
@Data
public class CustomerSaveDTO {

    @NotBlank(message = "客户名称不能为空")
    private String name;

    @NotBlank(message = "联系人不能为空")
    private String contact;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
