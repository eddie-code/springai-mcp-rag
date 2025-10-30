//package org.dromara.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.mcp.client.autoconfigure.NamedClientMcpTransport;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.http.HttpClient;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * https://lbs.amap.com/api/mcp-server/gettingstarted#t2
// * https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html
// */
//@Slf4j
//@Configuration
//public class McpConfig {
//
//    @Value("${GAODE_SSE_MCP_KEY}")
//    private String gaodeSseKey;
//
//    @Bean
//    public List<NamedClientMcpTransport> mcpClientTransport() {
//
//        List<NamedClientMcpTransport> transports = new ArrayList<>();
//
//        // 高德服务器配置
//        HttpClientSseClientTransport amapTransport = HttpClientSseClientTransport
//                .builder("https://mcp.amap.com")
//                .sseEndpoint("/sse?key=" + gaodeSseKey)
//                .objectMapper(new ObjectMapper())
//                .customizeClient(clientBuilder -> {
//                    // 设置连接超时时间为60秒
//                    clientBuilder.connectTimeout(Duration.ofSeconds(60));
//                    // 指定使用HTTP/1.1协议版本
//                    clientBuilder.version(HttpClient.Version.HTTP_1_1);
//                    // 设置重定向策略为NORMAL（自动跟随安全的重定向）
//                    // clientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
//                })
//                .build();
//        transports.add(new NamedClientMcpTransport("amap", amapTransport));
//
//        // 本地服务器配置
//        HttpClientSseClientTransport localTransport = HttpClientSseClientTransport
//                .builder("http://localhost:9060")
//                .sseEndpoint("/sse")
//                .objectMapper(new ObjectMapper())
//                .customizeClient(clientBuilder -> {
//                    clientBuilder.connectTimeout(Duration.ofSeconds(60));
//                })
//                .build();
//        transports.add(new NamedClientMcpTransport("local", localTransport));
//
//        // 单个Server
//        // return Collections.singletonList(new NamedClientMcpTransport("amap", transport));
//
//        // 返回多个连接配置
//        return transports;
//    }
//}
//
