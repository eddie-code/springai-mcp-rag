package org.dromara.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS配置类，用于配置跨域资源共享策略
 * 该类实现了WebMvcConfigurer接口，可以自定义Spring MVC的配置
 *
 * @author lee
 * @description
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${website.domain}")
    private String domain;

    /**
     * 添加CORS映射配置，允许跨域请求
     *
     * @param registry CORS注册器，用于配置跨域请求的映射规则
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置所有路径的跨域请求，允许来自任何源的请求
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 放开所有域名
                //.allowedOrigins(domain)
                //.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 指定请求方式
                .allowedMethods("*") // 放开所有的请求方式
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(60 * 60);
        ;
    }
}
