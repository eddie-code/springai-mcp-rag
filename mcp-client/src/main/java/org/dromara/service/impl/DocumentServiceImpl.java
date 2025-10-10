package org.dromara.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.service.IDocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lee
 * @description
 */
@Slf4j
@Service
public class DocumentServiceImpl implements IDocumentService {

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

        log.info("加载文本资源并转换为文档列表: {}", documents);

        return documents;
    }

}
