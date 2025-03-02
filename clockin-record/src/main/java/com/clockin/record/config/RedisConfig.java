package com.clockin.record.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis配置類
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置Redis緩存管理器
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 默認緩存1小時
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // 配置不同緩存分類的過期時間
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 打卡記錄相關緩存
        cacheConfigurations.put("clockInRecord", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 請假記錄相關緩存
        cacheConfigurations.put("leaveRequest", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 補打卡記錄相關緩存
        cacheConfigurations.put("makeupClockIn", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 用戶相關緩存
        cacheConfigurations.put("user", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        
        // 部門和崗位相關緩存
        cacheConfigurations.put("department", defaultCacheConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("position", defaultCacheConfig.entryTtl(Duration.ofHours(2)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * 配置RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用GenericJackson2JsonRedisSerializer序列化和反序列化redis的value值
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        
        // 值序列化方式採用JSON
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        
        // 鍵序列化方式採用String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        template.afterPropertiesSet();
        
        return template;
    }
}
