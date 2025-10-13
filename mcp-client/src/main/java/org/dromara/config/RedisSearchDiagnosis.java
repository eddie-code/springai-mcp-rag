package org.dromara.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author lee
 * @description 检查 RedisSearch 兼容性
 */
@Slf4j
//@Component
public class RedisSearchDiagnosis {

    private final RedisConnectionFactory connectionFactory;

    public RedisSearchDiagnosis(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void diagnose() {
        RedisConnection connection = null;
        try {
            connection = connectionFactory.getConnection();

            log.info("=== RedisSearch 诊断开始 ===");

            // 检查 FT.CREATE 命令支持
            checkCommandSupport(connection, "FT.CREATE");
            checkCommandSupport(connection, "FT.INFO");
            checkCommandSupport(connection, "FT.SEARCH");
            checkCommandSupport(connection, "FT._LIST");

            // 检查向量搜索功能
            checkVectorSupport(connection);

            log.info("=== RedisSearch 诊断完成 ===");

        } catch (Exception e) {
            log.error("诊断失败: {}", e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void checkCommandSupport(RedisConnection connection, String command) {
        try {
            // 尝试执行 HELP 命令查看命令信息
            connection.execute("HELP", command.getBytes());
            log.info("命令 {} 支持", command);
        } catch (Exception e) {
            log.warn("命令 {} 可能不支持: {}", command, e.getMessage());
        }
    }

    private void checkVectorSupport(RedisConnection connection) {
        try {
            // 尝试创建一个临时的测试索引
            String testIndex = "test_vector_index";

            List<byte[]> args = Arrays.asList(
                    testIndex.getBytes(),
                    "ON".getBytes(),
                    "HASH".getBytes(),
                    "PREFIX".getBytes(),
                    "1".getBytes(),
                    "test:".getBytes(),
                    "SCHEMA".getBytes(),
                    "vec".getBytes(), "VECTOR".getBytes(), "FLAT".getBytes(),
                    "TYPE".getBytes(), "FLOAT32".getBytes(),
                    "DIM".getBytes(), "2".getBytes()  // 使用小维度测试
            );

            connection.execute("FT.CREATE", args.toArray(new byte[0][]));
            log.info("向量搜索功能测试通过");

            // 清理测试索引
            connection.execute("FT.DROPINDEX", testIndex.getBytes());

        } catch (Exception e) {
            log.error("向量搜索功能测试失败: {}", e.getMessage());
        }
    }
}
