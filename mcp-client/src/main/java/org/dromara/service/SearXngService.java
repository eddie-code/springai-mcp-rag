package org.dromara.service;

import org.dromara.bean.SearchResult;

import java.util.List;

/**
 * @author lee
 * @description
 */
public interface SearXngService {

    /**
     * 调用本地搜索引擎-SearXng进行搜索
     *
     * @param query
     * @return
     */
    List<SearchResult> search(String query);

}
