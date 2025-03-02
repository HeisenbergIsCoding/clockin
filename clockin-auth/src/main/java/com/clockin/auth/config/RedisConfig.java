package com.clockin.auth.config;

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
        
        // 部門相關緩存配置
        cacheConfigurations.put("department", defaultCacheConfig.entryTtl(Duration.ofHours(3)));
        
        // 崗位相關緩存配置
        cacheConfigurations.put("position", defaultCacheConfig.entryTtl(Duration.ofHours(3)));
        
        // 用戶相關緩存配置
        cacheConfigurations.put("user", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        
        // 角色和權限相關緩存配置
        cacheConfigurations.put("role", defaultCacheConfig.entryTtl(Duration.ofHours(3)));
        cacheConfigurations.put("permission", defaultCacheConfig.entryTtl(Duration.ofHours(3)));

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
