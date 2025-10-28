[toc]

# 目录

## （一）MCP 服务平台的使用

* https://github.com/modelcontextprotocol/servers
* https://github.com/punkpeye/awesome-mcp-servers
* https://smithery.ai/
* https://bailian.console.aliyun.com/?tab=mcp#/mcp-market
* https://mcp.so/
* https://community.modelscope.cn/
* https://lbs.amap.com/api/mcp-server/gettingstarted

## 配置高德MCP服务器

[application.yml](../mcp-client/src/main/resources/application.yml)

```yaml
spring:
  ai:
    mcp:
      client:
        connection-timeout: 60s  # 增加到60秒
        request-timeout: 30s
        enabled: true
        name: spring-ai-mcp-client
        type: ASYNC
        sse:
          connections:
            server1:
              url: https://mcp.amap.com
              sse-endpoint: /sse?key=${GAODE_SSE_MCP_KEY}
        stdio:
          servers-configuration: classpath:mcp-server.json
```

[mcp-server.json](../mcp-client/src/main/resources/mcp-server.json)

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "E:/Program Files/nodejs/npx.cmd",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "D:/Develop/Mine/IdeaProjects/springai-mcp-rag/tmp"
      ]
    }
  }
}
```
`备注： NodeJS 需要完全放开权限， win系统：属性-权限-完全控制，否则出现：` [npm ERR! code EPERM npm ERR! syscall unlink npm ERR!错误](https://blog.csdn.net/weixin_45915647/article/details/132046959)