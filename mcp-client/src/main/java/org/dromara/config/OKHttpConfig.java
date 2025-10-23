package org.dromara.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author lee
 * @description 初始化OKHttp
 */
@Configuration
public class OKHttpConfig implements WebMvcConfigurer {

    /**
     * 创建并配置OkHttpClient实例
     * 设置连接超时时间为30秒，读取超时时间为60秒
     *
     * @return 配置好的OkHttpClient实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}