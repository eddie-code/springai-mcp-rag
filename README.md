# springai-mcp-rag

## （一）Redis Stack 

### 1.0 Redis命令行
docker exec -it redis bash

redis-cli -h 192.168.56.101 -p 6379 -a redis123 -n 10

### 1.1 切换到数据库 0
select 0

`Redis Stack 的搜索功能限制只能在数据库 0`

### 1.2 创建索引
FT.CREATE lee-vectorstore ON HASH PREFIX 1 "embedding:" SCHEMA content TEXT metadata TEXT embedding VECTOR FLAT 6 TYPE FLOAT32 DIM 1536 DISTANCE_METRIC COSINE

### 1.3 验证创建成功
FT._LIST

### 1.4 查看刚创建的索引信息
FT.INFO lee-vectorstore

### 1.5 测试插入数据
HSET embedding:doc1 content "测试文档" embedding "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

### 1.6 搜索测试
FT.SEARCH lee-vectorstore "*" LIMIT 0 10

### 1.7 删除索引
FT.DROPINDEX lee-vectorstore

### 1.8 删除索引但保留文档数据
FT.DROPINDEX lee-vectorstore DD


## (二) [错误信息](https://help.aliyun.com/zh/model-studio/error-code?spm=a2c4g.11186623.0.0.17c34380AA3kJh)

`首页大模型服务平台百炼API参考（模型）更多错误信息`

### 2.1 问题回顾 - 出现HTTP 404报错

* SpringAI+OpenAI+DeepSeek+RedisStack向量存储，出现HTTP 404报错
    * 使用DeepSeek聊天问答，是正常的。 比如请求：http://127.0.0.1:8080/hello/chat/stream/str?msg=99乘法表
    * org.dromara.service.impl.DocumentServiceImpl.redisVectorStore.add(list2); 会出现HTTP 404
      *  尝试使用本地tokenizer.json与model.onnx， 还是不行

错误配置如下：

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
    embedding:
      transformer:
        tokenizer:
          uri: classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json
        onnx:
          modelUri: classpath:/onnx/all-MiniLM-L6-v2/model.onnx
          modelOutputName: token_embeddings
        cache:
          enabled: true
          directory: classpath:/onnx
    vectorstore:
      redis:
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
```

* 暂时不知道怎么解决，放弃DeepSeek，转使用阿里云的《大模型服务平台百炼》
  * [首页大模型服务平台百炼API参考（模型）工具包/框架OpenAI兼容-Embedding](https://help.aliyun.com/zh/model-studio/embedding-interfaces-compatible-with-openai?spm=a2c4g.11186623.help-menu-2400256.d_2_10_5.59e8177aroxPOA#4f78c829c5dwz)
  * 切换APIKey与BASE_URL， 添加 spring.ai.embedding.options.model=text-embedding-v4 就能向量存储

`批次大小指单次API调用中能处理的文本数量上限。例如，text-embedding-v4的批次大小为10，意味着一次请求最多可传入10个文本进行向量化。这个限制适用于： 字符串数组输入：数组最多包含10个元素。 文件输入：文本文件最多包含10行文本。`

配置如下：

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
      embedding:
        options:
          model: text-embedding-v4
    vectorstore:
      redis:
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
```


### 2.2 问题回顾 - 出现HTTP 400

报错信息：HTTP 400 - {"error":{"code":"InvalidParameter","param":null,"message":"<400> InternalError.Algo.InvalidParameter: Value error, batch size is invalid, it should not be larger than 10.: input.contents","type":"InvalidParameter"},

原因： 调用 Embedding 模型时，文本数量超过模型上限。

解决方案： 参考[Embedding](https://help.aliyun.com/zh/model-studio/embedding?spm=a2c4g.11186623.0.0.2acd177azHoZVH)文档中模型的批次大小信息，控制传入文本的数量。 可以切换为 `text-embedding-async-v1`
