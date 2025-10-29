package org.dromara.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lee
 * @description
 */
@Slf4j
@Component
public class DateTool {

    /**
     * 根据城市所在的时区id来获取当前的时间
     * 
     * @param cityName 城市名称，用于日志记录
     * @param zoneId 时区ID，用于指定需要获取时间的时区
     * @return 格式化后的当前时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     */
    @Tool(description = "根据城市所在的时区id来获取当前的时间")
    public String getCurrentTimeByZoneId(String cityName, String zoneId) {
        log.info("========= 调用MCP工具：getCurrentTimeByZoneId()===========");
        log.info(String.format("========= 参数cityName: %s ===========", cityName));
        log.info(String.format("========= 参数zoneId: %s ===========", zoneId));
        ZoneId zone = ZoneId.of(zoneId);
        // 获取该时区对应的当前时间
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);
        String currentTime = String.format("当前的时间是%s", zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("根据城市所在的时区id来获取当前的时间：{}", currentTime);
        return currentTime;
    }

    /**
     * 获得当前时间
     * 
     * @return 格式化后的当前时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     */
    @Tool(description = "获得当前时间")
    public String getCurrentTime() {
        log.info("========= 调用MCP工具：getCurrentTime()===========");
        String currentTime = String.format("当前的时间是%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("获得当前时间：{}", currentTime);
        return currentTime;
    }

}
