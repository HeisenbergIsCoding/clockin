package com.clockin.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 配置類
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    
    /**
     * JWT 密鑰
     */
    private String secret;
    
    /**
     * JWT 過期時間（秒）
     */
    private Long expiration;
}
