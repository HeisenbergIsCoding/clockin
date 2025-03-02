package com.clockin.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Google OAuth2配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "google.client")
public class GoogleOAuth2Config {

    /**
     * Google客戶端ID
     */
    private String clientId;

    /**
     * Google客戶端密鑰
     */
    private String clientSecret;

    /**
     * 重定向URI
     */
    private String redirectUri;
}
