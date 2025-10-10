package org.dromara;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.ai.model.transformers.autoconfigure.TransformersEmbeddingModelAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * Hello world!
 */
//@SpringBootApplication
@SpringBootApplication(exclude = {TransformersEmbeddingModelAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        //System.out.println("Hello World!");

        // 加载.env文件
        Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().load();
        // 把.env文件中的变量设置到环境变量中
        dotenv.entries().forEach(dotenvEntry ->
                System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
        );
//        System.setProperty("spring.ai.cache.enabled", "false");
        SpringApplication.run(App.class, args);
    }
}
