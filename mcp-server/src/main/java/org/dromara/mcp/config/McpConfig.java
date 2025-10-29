package org.dromara.mcp.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.mcp.tools.DateTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lee
 * @description
 */
@Slf4j
@Configuration
public class McpConfig {


    /**
     * 注册MCP工具
     *
     * @param dateTool
     * @return
     */
    @Bean
    public ToolCallbackProvider registMCPTools(DateTool dateTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTool)
                .build();
    }


}
