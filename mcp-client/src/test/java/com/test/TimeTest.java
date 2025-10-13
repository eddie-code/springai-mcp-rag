package com.test;

import org.dromara.App;
import org.junit.jupiter.api.Test;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.Map;

/**
 * @author lee
 * @description
 */
@SpringBootTest(classes = App.class)
public class TimeTest {

    @Test
    public void testTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("任务1");
        Thread.sleep(1000);
        stopWatch.stop();

        stopWatch.start("任务2");
        Thread.sleep(300);
        stopWatch.stop();

        stopWatch.start("任务3");
        Thread.sleep(100);
        stopWatch.stop();

        // 打印任务的耗时统计
        System.out.println(stopWatch.prettyPrint());
//        System.out.println(stopWatch.shortSummary());

        // 任务总览s
        System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务总数：" + stopWatch.getTaskCount());
    }

    @Test
    public void classEmbeddingTest() throws Exception {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        // 设置tokenizer文件路径
        embeddingModel.setTokenizerResource("classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json");
        // 设置Onnx模型文件路径
        embeddingModel.setModelResource("classpath:/onnx/all-MiniLM-L6-v2/model.onnx");
        // 缓存位置
        embeddingModel.setResourceCacheDirectory("/tmp/onnx-cache");
        // 自动填充
        embeddingModel.setTokenizerOptions(Map.of("padding", "true"));
        // 模型输出层的名称，默认是 last_hidden_state, 需要根据所选模型设置
        embeddingModel.setModelOutputName("token_embeddings");
        embeddingModel.afterPropertiesSet();
        String text = "你好，小爱同学";
        long t = System.currentTimeMillis();
        // 生成文本嵌入向量
        float[] embed = embeddingModel.embed(text);
        long useTime = System.currentTimeMillis() - t;
        System.out.println("embed finish: " + text + " ,len: " + embed.length + "  UseTime：" + useTime + "ms");
        for (float f : embed) System.out.print(f);
    }


}
