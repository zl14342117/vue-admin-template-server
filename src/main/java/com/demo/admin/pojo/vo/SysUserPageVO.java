package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统用户分页出参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserPageVO {

    private Long total;

    private List<SysUserVO> list;
}
