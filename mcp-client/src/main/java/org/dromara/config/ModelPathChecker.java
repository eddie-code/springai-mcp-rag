package org.dromara.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
//@Component
public class ModelPathChecker {

    @Value("${spring.ai.embedding.transformer.onnx.modelUri}")
    private String modelUri;

    @Value("${spring.ai.embedding.transformer.tokenizer.uri}")
    private String tokenizerUri;

    @PostConstruct
    public void checkPaths() {

        log.info("=== Model 连接诊断开始 ===");

        log.info("OPENAI_API_KEY: " + System.getenv("OPENAI_API_KEY"));
        log.info("OPENAI_BASE_URL: " + System.getenv("OPENAI_BASE_URL"));
        log.info("OPENAI_MODEL: " + System.getenv("OPENAI_MODEL"));

        log.info("Model URI: " + modelUri);
        log.info("Tokenizer URI: " + tokenizerUri);

        try {
            // 检查文件是否存在，区分 classpath 和 file 协议
            if (modelUri.startsWith("classpath:")) {
                Resource modelResource = new ClassPathResource(modelUri.substring("classpath:".length()));
                Resource tokenizerResource = new ClassPathResource(tokenizerUri.substring("classpath:".length()));
                
                log.info("Model file exists: " + modelResource.exists());
                log.info("Tokenizer file exists: " + tokenizerResource.exists());
                
                if (modelResource.exists()) {
                    log.info("Model file size: " + modelResource.contentLength() + " bytes");
                }
            } else if (modelUri.startsWith("file:")) {
                // 处理文件系统路径
                Path modelPath = Path.of(modelUri.substring("file:".length()));
                Path tokenizerPath = Path.of(tokenizerUri.substring("file:".length()));
                
                log.info("Model file exists: " + Files.exists(modelPath));
                log.info("Tokenizer file exists: " + Files.exists(tokenizerPath));
                
                if (Files.exists(modelPath)) {
                    log.info("Model file size: " + Files.size(modelPath) + " bytes");
                }
            }

            log.info("=== Model 连接诊断结束 ===");

        } catch (Exception e) {
            System.err.println("Error checking paths: " + e.getMessage());
            e.printStackTrace();
        }
    }
}