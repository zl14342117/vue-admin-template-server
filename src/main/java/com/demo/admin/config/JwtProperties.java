package com.demo.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置，对应 application.yml 的 jwt.*。
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** 签名密钥 */
    private String secret = "liteops-vue-admin-template-jwt-secret-key-2026";

    /** 过期小时数 */
    private long expireHours = 24L;
}
