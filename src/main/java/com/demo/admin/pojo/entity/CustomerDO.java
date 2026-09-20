package com.demo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户表实体，对应表 sys_customer。
 */
@Data
@TableName("sys_customer")
public class CustomerDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String customerNo;

    private String name;

    private String contact;

    private String phone;

    /** 状态：1 启用，0 禁用 */
    private Integer status;

    private LocalDateTime createdAt;
}
