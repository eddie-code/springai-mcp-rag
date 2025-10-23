package org.dromara.service.impl;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.dromara.bean.SearXNGResponse;
import org.dromara.bean.SearchResult;
import org.dromara.service.SearXngService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author lee
 * @description
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearXngServiceImpl implements SearXngService {

    @Value("${internet.websearch.searxng.url}")
    private String SEARXNG_URL;

    @Value("${internet.websearch.searxng.count}")
    private Integer COUNT;

    private final OkHttpClient okHttpClient;

    /**
     * 根据查询关键字搜索相关信息
     * 通过SearXNG搜索引擎API进行搜索，设置查询参数并处理返回结果
     *
     * @param query 搜索关键字
     * @return 搜索结果列表，包含 SearchResult 对象
     */
    @Override
    public List<SearchResult> search(String query) {

        // 使用SEARXNG_URL作为基础URL构建HttpUrl对象，并添加查询参数
        // q: 搜索关键字
        // format: 指定返回格式为json
        HttpUrl url = HttpUrl.get(SEARXNG_URL)
                .newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("format", "json")
//            .addQueryParameter("engines","bing")
                .build();

        // 记录构建好的搜索URL，便于调试和日志追踪
        log.info("搜索的url地址为：" + url.url());

        // 定义SearXNG必需的Cookie头信息
        String cookies = "categories=general; language=zh-CN; locale=zh-Hans-CN; autocomplete=; favicon_resolver=; image_proxy=0; method=POST; safesearch=0; theme=simple; results_on_new_tab=0; doi_resolver=oadoi.org; simple_style=auto; center_alignment=0; advanced_search=0; query_in_title=0; infinite_scroll=0; search_on_category_select=1; hotkeys=default; url_formatting=pretty; disabled_engines=\"wikipedia__general\\054currency__general\\054wikidata__general\\054duckduckgo__general\\054google__general\\054lingva__general\\054startpage__general\\054dictzone__general\\054mymemory translated__general\\054brave__general\"; enabled_engines=\"baidu__general\\054bing__general\\054sogou__general\"; disabled_plugins=; enabled_plugins=; tokens=";

        // 创建HTTP请求对象，设置请求URL和必要的Cookie头
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookies)
                .build();

        // 发送HTTP请求并获取响应，使用try-with-resources确保资源正确关闭
        try (Response response = okHttpClient.newCall(request).execute()) {

            // 检查HTTP响应状态码，如果不是2xx则记录详细错误信息并抛出运行时异常
            if (!response.isSuccessful()) {
                String errorMessage = String.format("SearXNG请求失败: HTTP %d, URL: %s", response.code(), url.url());
                if (response.body() != null) {
                    // 尝试读取错误响应体以获取更多错误信息
                    try {
                        String errorBody = response.body().string();
                        errorMessage += ", 错误详情: " + errorBody;
                        log.error("SearXNG服务返回错误，HTTP状态码: {}, 错误详情: {}", response.code(), errorBody);
                    } catch (IOException ioException) {
                        log.warn("无法读取错误响应体: {}", ioException.getMessage());
                    }
                } else {
                    log.error("SearXNG服务返回错误，HTTP状态码: {}", response.code());
                }
                throw new RuntimeException(errorMessage);
            }

            // 检查响应体是否为空
            if (response.body() != null) {
                // 读取响应体内容为字符串
                String responseBody = response.body().string();
                // 记录原始响应内容，便于调试
                log.info("SearXNG原始返回：{}", responseBody);

                // 将响应的JSON字符串转换为SearXNGResponse对象
                SearXNGResponse searXNGResponse = JSONUtil.toBean(responseBody, SearXNGResponse.class);

                // 处理搜索结果，排序并限制数量后返回
                return dealResults(searXNGResponse.getResults());
            }
            // 如果响应体为空，记录错误日志
            log.error("搜索失败：响应体为空，HTTP状态码: {}", response.code());
        } catch (IOException e) {
            // 捕获IO异常并包装为运行时异常抛出
            String errorMessage = "网络请求异常: " + e.getMessage();
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        // 如果搜索过程出现异常或未获取到结果，返回空列表
        return Collections.emptyList();
    }

    /**
     * 处理结果集，截取限制的个数
     * 对搜索结果进行排序并限制返回数量
     *
     * @param results 搜索结果列表
     * @return 处理后的结果列表，按分数倒序排列并限制数量
     */
    private List<SearchResult> dealResults(List<SearchResult> results) {

        // 检查结果列表是否为空或长度为0
        if (CollectionUtils.isEmpty(results)) {
            // 如果结果为空，返回空列表
            return Collections.emptyList();
        }

        try {
            // 对结果进行处理：
            // 1. 截取前COUNT个结果（如果结果数不足COUNT个则取全部）
            // 2. 使用并行流处理提高性能
            // 3. 按照搜索结果的分数降序排序
            // 4. 限制最终结果数量为COUNT个
            List<SearchResult> list = results.subList(0, Math.min(COUNT, results.size()))
                    .parallelStream()
                    .sorted(Comparator.comparing(SearchResult::getScore).reversed())
                    .limit(COUNT).toList();

            // 返回处理后的结果列表
            return list;
        } catch (Exception e) {
            log.error("处理搜索结果时发生异常: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}

