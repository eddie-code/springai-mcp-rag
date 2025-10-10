package org.dromara.service;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author lee
 * @description
 */
public interface IDocumentService {


    /**
     * 加载文档并且读取数据进行保存到知识库
     *
     * @param resource 要加载的资源对象
     * @param fileName 目标文件名称
     */
    List<Document> loadText(Resource resource, String fileName);


}
