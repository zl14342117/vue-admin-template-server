package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统角色分页出参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePageVO {

    private Long total;

    private List<SysRoleVO> list;
}
