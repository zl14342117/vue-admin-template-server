package com.demo.admin.util;

import com.demo.admin.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * JWT 签发与解析。
 */
@Component
public class JwtUtil {

    private static final String CLAIM_ROLE = "role";

    @Resource
    private JwtProperties jwtProperties;

    public String createToken(String username, String role) {
        long now = System.currentTimeMillis();
        long expireMillis = jwtProperties.getExpireHours() * 3600L * 1000L;
        return Jwts.builder()
                .setSubject(username)
                .claim(CLAIM_ROLE, role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireMillis))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 解析 token，失败返回 null。
     */
    public Claims parseToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims == null ? null : claims.getSubject();
    }
}
