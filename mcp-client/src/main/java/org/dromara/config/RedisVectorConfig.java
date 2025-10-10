package org.dromara.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 向量存储创建专用的 Redis 配置
 * <p>
 * 使用数据库 0 专门用于 Spring AI 的向量存储，其他业务数据使用其他数据库。这是因为：
 * 1、Redis Stack 的搜索功能限制只能在数据库 0
 * 2、向量存储数据通常与其他业务数据隔离
 * 3、性能考虑，避免搜索索引影响其他操作
 * 如果你的应用已经在数据库 10 有大量数据，就使用此配置《RedisVectorConfig》
 *
 * @author lee
 * @description
 */
//@Configuration
public class RedisVectorConfig {

    @Bean
    @Primary
    public RedisConnectionFactory vectorRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("192.168.56.101");
        config.setPort(6379);
        config.setDatabase(0);  // 向量存储用数据库 0
        return new LettuceConnectionFactory(config);
    }

    @Bean(name = "businessRedisConnectionFactory")
    public RedisConnectionFactory businessRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("192.168.56.101");
        config.setPort(6379);
        config.setDatabase(10);  // 业务数据用数据库 10
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> vectorRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(vectorRedisConnectionFactory());
        return template;
    }

    @Bean(name = "businessRedisTemplate")
    public RedisTemplate<String, Object> businessRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(businessRedisConnectionFactory());
        return template;
    }
}
