package org.dromara;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        //System.out.println("Hello World!");

        // 加载.env文件
        Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().load();
        // 把.env文件中的变量设置到环境变量中
        dotenv.entries().forEach(dotenvEntry ->
                System.setProperty(dotenvEntry.getKey(), dotenvEntry.getValue())
        );

        SpringApplication.run(App.class, args);
    }
}
