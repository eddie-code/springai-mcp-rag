package org.dromara.mcp.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.mcp.tools.DateTool;
import org.dromara.mcp.tools.EmailTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP配置类
 * 用于配置MCP相关的Bean组件
 *
 * @author lee
 * @description
 */
@Slf4j
@Configuration
public class McpConfig {


    /**
     * 注册MCP工具
     * 将日期工具和邮件工具注册为可回调的工具提供者
     *
     * @param dateTool  日期工具实例
     * @param emailTool 邮件工具实例
     * @return ToolCallbackProvider 工具回调提供者实例
     */
    @Bean
    public ToolCallbackProvider registMCPTools(DateTool dateTool, EmailTool emailTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool, emailTool)
                .build();
    }


}

