package org.dromara.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.service.IDocumentService;
import org.dromara.utils.CustomTextSpliter;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author lee
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements IDocumentService {

    private final RedisVectorStore redisVectorStore;

    /**
     * 加载文本资源并转换为文档列表
     *
     * @param resource 要加载的资源对象
     * @param fileName 文件名称，用于设置自定义元数据
     * @return 包含文本内容的文档列表
     */
    @Override
    public List<Document> loadText(Resource resource, String fileName) {
        // 创建文本读取器实例
        TextReader textReader = new TextReader(resource);
        // 将文件名添加到自定义元数据中
        textReader.getCustomMetadata().put("fileName", fileName);
        // 获取并返回文档列表
        List<Document> documents = textReader.get();

//        log.info("加载文本资源并转换为文档列表: {}", documents);

        // 默认的文本切分器：创建TokenTextSplitter实例，用于将文档按照token进行分割
//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        // 自定义的切分
        CustomTextSpliter tokenTextSplitter = new CustomTextSpliter();
        // 应用token分割器处理文档列表，将输入的文档集合转换为分割后的文档列表
        List<Document> list = tokenTextSplitter.apply(documents);

        log.info("应用token分割器处理文档Size: {}", list.size());
        log.info("应用token分割器处理文档列表: {}", list);
        log.info("===============================");

        redisVectorStore.add(list);
        log.info("文档成功添加到向量存储");
        return documents;
    }

    @Override
    public List<Document> doSearch(String question) {
        log.info("搜索问题: {}", question);
        // 构建搜索请求，设置查询文本和返回的文档数量
        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(3)
                .build();
        List<Document> results = redisVectorStore.similaritySearch(request);
        log.info("搜索结果数量: {}", results.size());
        log.info("搜索结果: {}", results);
        return results;
    }

}
