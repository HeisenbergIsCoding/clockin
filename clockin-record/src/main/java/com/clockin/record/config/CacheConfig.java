package com.clockin.record.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 緩存配置
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置緩存管理器
     *
     * @param redisConnectionFactory Redis連接工廠
     * @return 緩存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 預設配置，所有緩存存活時間為1小時
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 特定緩存配置，不同緩存可以設置不同的存活時間
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        // 打卡記錄緩存配置，存活時間30分鐘
        configMap.put("clockInRecord", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        // 打卡匯總緩存配置，存活時間1天
        configMap.put("clockInSummary", defaultConfig.entryTtl(Duration.ofDays(1)));
        // 每月統計緩存配置，存活時間7天
        configMap.put("monthlyStatistics", defaultConfig.entryTtl(Duration.ofDays(7)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configMap)
                .build();
    }
}
