[toc]

# 目录

## Maven

```xml
<!--        从Hugging face拉取模型-->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-transformers</artifactId>
</dependency>
```

## Github下载文件

[onnx.all-MiniLM-L6-v2](https://github.com/spring-projects/spring-ai/tree/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2) 下载文件
* [tokenizer.json](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2/tokenizer.json)
* [model.onnx](https://media.githubusercontent.com/media/spring-projects/spring-ai/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2/model.onnx)
* [model.png](https://github.com/spring-projects/spring-ai/blob/main/models/spring-ai-transformers/src/main/resources/onnx/all-MiniLM-L6-v2/model.png)

## [application.yml](../mcp-client/src/main/resources/application.yml) 配置 ONNX 嵌入模块

```yaml
spring:
  application:
    name: spring-ai-mcp-client
  profiles:
    active: dev
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: ${OPENAI_BASE_URL}
      chat:
        options:
          model: ${OPENAI_MODEL}
#      embedding:
#        options:
#          model: text-embedding-v2
    embedding:
      onnx:
        enabled: true
        options:
          model: all-MiniLM-L6-v2
        transformer:
          tokenizer:
            uri: classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json
          onnx:
            model-uri: classpath:/onnx/all-MiniLM-L6-v2/model.onnx
    vectorstore:
      redis:
        # 必须启动时候自动创建'索引', 不然会出现查询不到数据等问题
        initialize-schema: true
        index-name: lee-vectorstore
        prefix: 'embedding:'
  data:
    redis:
      host: 192.168.56.101
      port: 6379
      password: redis123
      timeout: 10s
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0

logging:
  level:
    root: info
```

## 启动日志

```text
2025-10-29T20:54:01.141+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] org.dromara.McpClientApplication         : Starting McpClientApplication using Java 21.0.5 with PID 38960 (D:\Develop\Mine\IdeaProjects\springai-mcp-rag\mcp-client\target\classes started by Eddie in D:\Develop\Mine\IdeaProjects\springai-mcp-rag)
2025-10-29T20:54:01.146+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] org.dromara.McpClientApplication         : The following 1 profile is active: "dev"
2025-10-29T20:54:02.420+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode
2025-10-29T20:54:02.423+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Redis repositories in DEFAULT mode.
2025-10-29T20:54:02.461+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 19 ms. Found 0 Redis repository interfaces.
2025-10-29T20:54:03.181+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-10-29T20:54:03.203+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-10-29T20:54:03.203+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.41]
2025-10-29T20:54:03.300+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-10-29T20:54:03.301+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1801 ms
2025-10-29T20:54:04.827+08:00  INFO 38960 --- [spring-ai-mcp-client] [ient-3-Worker-0] i.m.client.McpAsyncClient                : Server response with Protocol: 2024-11-05, Capabilities: ServerCapabilities[completions=CompletionCapabilities[], experimental=null, logging=LoggingCapabilities[], prompts=PromptCapabilities[listChanged=true], resources=ResourceCapabilities[subscribe=false, listChanged=true], tools=ToolCapabilities[listChanged=true]], Info: Implementation[name=spring-ai-mcp-server-sse, version=1.0.0] and Instructions null
2025-10-29T20:54:05.472+08:00  INFO 38960 --- [spring-ai-mcp-client] [ient-4-Worker-1] i.m.client.McpAsyncClient                : Server response with Protocol: 2024-11-05, Capabilities: ServerCapabilities[completions=null, experimental=null, logging=LoggingCapabilities[], prompts=null, resources=null, tools=ToolCapabilities[listChanged=true]], Info: Implementation[name=amap-sse-server, version=1.0.0] and Instructions null
2025-10-29T20:54:08.230+08:00  INFO 38960 --- [spring-ai-mcp-client] [pool-5-thread-1] i.m.c.transport.StdioClientTransport     : STDERR Message received: Secure MCP Filesystem Server running on stdio
2025-10-29T20:54:08.247+08:00  INFO 38960 --- [spring-ai-mcp-client] [pool-2-thread-1] i.m.client.McpAsyncClient                : Server response with Protocol: 2024-11-05, Capabilities: ServerCapabilities[completions=null, experimental=null, logging=null, prompts=null, resources=null, tools=ToolCapabilities[listChanged=null]], Info: Implementation[name=secure-filesystem-server, version=0.2.0] and Instructions null
2025-10-29T20:54:08.253+08:00  INFO 38960 --- [spring-ai-mcp-client] [pool-5-thread-1] i.m.c.transport.StdioClientTransport     : STDERR Message received: Client does not support MCP Roots, using allowed directories set from server args: [ 'D:\\Develop\\Mine\\IdeaProjects\\springai-mcp-rag\\tmp' ]
2025-10-29T20:54:10.109+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] ai.djl.util.Platform                     : Found matching platform from: jar:file:/E:/Develop/Apache/MavenRepository/default/ai/djl/huggingface/tokenizers/0.32.0/tokenizers-0.32.0.jar!/native/lib/tokenizers.properties
2025-10-29T20:54:10.818+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.a.t.TransformersEmbeddingModel       : Model input names: input_ids, attention_mask, token_type_ids
2025-10-29T20:54:10.818+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.a.t.TransformersEmbeddingModel       : Model output names: last_hidden_state
2025-10-29T20:54:12.032+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-10-29T20:54:12.044+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] org.dromara.McpClientApplication         : Started McpClientApplication in 11.594 seconds (process running for 12.729)
2025-10-29T20:54:12.047+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.dromara.config.RedisDiagnosisConfig    : === Redis 连接诊断开始 ===
2025-10-29T20:54:12.086+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.dromara.config.RedisDiagnosisConfig    : Redis Ping: PONG
2025-10-29T20:54:12.089+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.dromara.config.RedisDiagnosisConfig    : Redis 模块信息: {search_largest_memory_index=16064, search_number_of_active_indexes_indexing=0, search_minimal_term_prefix=2, search_total_docs_not_collected_by_gc=0, search_max_doc_table_size=1000000, search_fields_text=Text=1,IndexErrors=0, search_total_query_execution_time_ms=0, search_used_memory_indexes=16064, search_global_idle=0, search_OOM_indexing_failures_indexes_count=0, search_cursor_max_idle_time=300000, search_total_queries_processed=0, module=name=search,ver=21020,api=1,filters=0,usedby=[],using=[ReJSON],options=[handle-io-errors], search_smallest_memory_index_human=0.01531982421875, search_max_search_results=10000, search_marked_deleted_vectors=0, search_min_phonetic_term_length=3, redisgears_2_backend_name=js, search_dialect_3=0, search_used_memory_indexes_human=0.01531982421875, search_dialect_2=0, search_dialect_1=0, search_enableGC=ON, search_dialect_4=0, search_maximal_prefix_expansions=200, search_total_active_queries=0, search_max_aggregate_results=10000, search_total_indexing_time=0, search_total_query_commands=0, search_gc_scan_size=100, search_cursor_read_size=1000, search_number_of_active_indexes=0, search_used_memory_vector_index=0, search_total_cycles=0, search_total_ms_run=0, search_number_of_active_indexes_running_queries=0, search_errors_for_index_with_max_failures=0, search_number_of_indexes=1, search_timeout_policy=return, search_fields_vector=Vector=1,HNSW=1,IndexErrors=0, search_errors_indexing_failures=0, search_bm25std_tanh_factor=4, search_redis_version=7.4.6 - oss, search_minimal_stem_length=4, search_smallest_memory_index=16064, search_query_timeout_ms=500, search_bytes_collected=0, search_total_active_write_threads=0, search_version=2.10.20, search_largest_memory_index_human=0.01531982421875, search_global_total=0}
2025-10-29T20:54:12.091+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.dromara.config.RedisDiagnosisConfig    : RedisSearch 模块可用
2025-10-29T20:54:12.091+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.dromara.config.RedisDiagnosisConfig    : === Redis 连接诊断完成 ===
```

### 1. 模型初始化成功

```text
2025-10-29T20:54:10.818+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.a.t.TransformersEmbeddingModel       : Model input names: input_ids, attention_mask, token_type_ids
2025-10-29T20:54:10.818+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] o.s.a.t.TransformersEmbeddingModel       : Model output names: last_hidden_state
```

这两行日志明确表明：

    ✅ ONNX 模型文件已正确解析

    ✅ 模型输入输出层已成功识别

    ✅ TransformersEmbeddingModel 已就绪

### 2. 本地加载的证据

```text
2025-10-29T20:54:10.109+08:00  INFO 38960 --- [spring-ai-mcp-client] [           main] ai.djl.util.Platform                     : Found matching platform from: jar:file:/E:/Develop/Apache/MavenRepository/default/ai/djl/huggingface/tokenizers/0.32.0/tokenizers-0.32.0.jar!/native/lib/tokenizers.properties
```

这行日志显示：

    ✅ 使用了本地的 tokenizers 库

    ✅ 没有出现 GitHub 下载相关的日志

    ✅ 没有出现模型缓存或下载的 INFO 消息

### 3. 结论

ONNX 嵌入模块已经完全成功加载！ 您的配置修改已经生效，现在模型是从本地 classpath 加载的，而不是从 GitHub 下载。

您可以放心使用嵌入功能进行向量化操作了。