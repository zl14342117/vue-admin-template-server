package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录成功后返回的 token 信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {

    private String token;
}
