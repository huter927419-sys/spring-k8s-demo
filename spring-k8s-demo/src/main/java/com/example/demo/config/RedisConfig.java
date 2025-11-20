package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * RedisConfig - Redis Configuration Class
 * Redis 配置类
 * 
 * @author Spring K8s Demo Team
 * @version 1.0.0
 * @since 2025-11-20
 * 
 * @description
 * This configuration class sets up Redis for caching and session management.
 * It configures ObjectMapper for Java 8 date/time support, RedisTemplate for
 * direct Redis operations, and RedisCacheManager for Spring Cache abstraction.
 * 
 * 该配置类设置 Redis 用于缓存和会话管理。
 * 它配置 ObjectMapper 以支持 Java 8 日期/时间，配置 RedisTemplate 用于
 * 直接 Redis 操作，配置 RedisCacheManager 用于 Spring Cache 抽象。
 */
@Configuration
@EnableCaching
public class RedisConfig {
    
    /**
     * Configure ObjectMapper for Java 8 date/time serialization
     * 配置 ObjectMapper 以支持 Java 8 日期/时间序列化
     * 
     * @return Configured ObjectMapper instance / 配置的 ObjectMapper 实例
     * @description
     * Creates an ObjectMapper with JavaTimeModule to support LocalDateTime
     * and other Java 8 time types. Disables timestamp format for ISO-8601 strings.
     * 
     * 创建一个带有 JavaTimeModule 的 ObjectMapper 以支持 LocalDateTime
     * 和其他 Java 8 时间类型。禁用时间戳格式以使用 ISO-8601 字符串。
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register Java 8 time module for LocalDateTime support
        // 注册 Java 8 时间模块以支持 LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        // Use ISO-8601 format instead of timestamps
        // 使用 ISO-8601 格式而不是时间戳
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    
    /**
     * Create JSON serializer with custom ObjectMapper
     * 使用自定义 ObjectMapper 创建 JSON 序列化器
     * 
     * @param objectMapper Configured ObjectMapper / 配置的 ObjectMapper
     * @return GenericJackson2JsonRedisSerializer instance / GenericJackson2JsonRedisSerializer 实例
     * @description
     * Creates a JSON serializer that uses the configured ObjectMapper
     * to properly serialize Java 8 date/time types.
     * 
     * 创建一个使用配置的 ObjectMapper 的 JSON 序列化器
     * 以正确序列化 Java 8 日期/时间类型。
     */
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(ObjectMapper objectMapper) {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
    
    /**
     * Configure RedisTemplate for direct Redis operations
     * 配置 RedisTemplate 用于直接 Redis 操作
     * 
     * @param connectionFactory Redis connection factory / Redis 连接工厂
     * @param jsonSerializer JSON serializer for values / 用于值的 JSON 序列化器
     * @return Configured RedisTemplate / 配置的 RedisTemplate
     * @description
     * Configures RedisTemplate with String serializer for keys and JSON serializer
     * for values to support complex object serialization.
     * 
     * 使用字符串序列化器用于键和 JSON 序列化器用于值来配置 RedisTemplate，
     * 以支持复杂对象序列化。
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, 
                                                       GenericJackson2JsonRedisSerializer jsonSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Use String serializer for keys / 对键使用字符串序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * Configure RedisCacheManager for Spring Cache
     * 配置 RedisCacheManager 用于 Spring Cache
     * 
     * @param connectionFactory Redis connection factory / Redis 连接工厂
     * @param jsonSerializer JSON serializer for cache values / 用于缓存值的 JSON 序列化器
     * @return Configured RedisCacheManager / 配置的 RedisCacheManager
     * @description
     * Configures RedisCacheManager with 1-hour TTL, JSON serialization,
     * and null value caching disabled.
     * 
     * 配置 RedisCacheManager，TTL 为 1 小时，使用 JSON 序列化，
     * 并禁用 null 值缓存。
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          GenericJackson2JsonRedisSerializer jsonSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            // Set cache TTL to 1 hour / 设置缓存 TTL 为 1 小时
            .entryTtl(Duration.ofHours(1))
            // Use String serializer for keys / 对键使用字符串序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // Use JSON serializer for values / 对值使用 JSON 序列化器
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
            // Disable caching null values / 禁用缓存 null 值
            .disableCachingNullValues();
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
