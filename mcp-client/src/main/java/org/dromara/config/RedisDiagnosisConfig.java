package org.dromara.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Properties;

/**
 * @author lee
 * @description 检测Redis是否连接正常
 */
@Slf4j
@Configuration
public class RedisDiagnosisConfig {

    @Bean
    public CommandLineRunner redisDiagnosis(RedisConnectionFactory redisConnectionFactory) {
        return args -> {
            log.info("=== Redis 连接诊断开始 ===");

            try {
                // 测试基础连接
                RedisConnection connection = redisConnectionFactory.getConnection();
                String pingResult = connection.ping();
                log.info("Redis Ping: {}", pingResult);

                // 检查模块
                Properties info = connection.info("modules");
                log.info("Redis 模块信息: {}", info);

                // 检查现有索引
                try {
                    connection.execute("FT._LIST");
                    log.info("RedisSearch 模块可用");
                } catch (Exception e) {
                    log.error("RedisSearch 模块不可用: {}", e.getMessage());
                }

                connection.close();
                log.info("=== Redis 连接诊断完成 ===");

            } catch (Exception e) {
                log.error("Redis 连接诊断失败: {}", e.getMessage(), e);
            }
        };
    }
}
