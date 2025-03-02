package com.clockin.auth.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 緩存配置類
 * <p>
 * 啟用Spring緩存功能，緩存管理配置見RedisConfig
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // 緩存實際配置已移至RedisConfig類
}
