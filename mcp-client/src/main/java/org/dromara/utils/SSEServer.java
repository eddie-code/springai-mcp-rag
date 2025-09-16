package org.dromara.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * SSE服务器类，用于管理SSE连接和消息推送
 *
 * @author lee
 * @description
 */
@Slf4j
public class SSEServer {

    // 存放所有用户
    public static final Map<String, Object> sseClients = new ConcurrentHashMap<>();

    /**
     * 连接SSE服务
     *
     * @param userId 用户ID，用于标识唯一的SSE连接
     * @return SseEmitter对象，用于向客户端推送消息
     */
    public static SseEmitter connect(String userId) {

        // 设置超时时间, 0L表示不超时（永不过期）; 默认：30s, 超时未完成的任务则会抛出异常
        SseEmitter sseEmitter = new SseEmitter(0L);

        // 注册回调方法
        sseEmitter.onTimeout(timeoutCallback(userId));
        // 完成
        sseEmitter.onCompletion(completionCallback(userId));
        // 异常
        sseEmitter.onError(errorCallback(userId));

        sseClients.put(userId, sseEmitter);

        log.info("SSE连接创建成功, 连接的用户ID为：{}", userId);

        return sseEmitter;
    }

    /**
     * 创建超时回调函数
     *
     * @param userId 用户ID
     * @return Runnable回调函数
     */
    public static Runnable timeoutCallback(String userId) {
        return () -> {
            log.info("SSE超时...");
            // 移除用户链接
            remove(userId);
        };
    }

    /**
     * 创建完成回调函数
     *
     * @param userId 用户ID
     * @return Runnable回调函数
     */
    public static Runnable completionCallback(String userId) {
        return () -> {
            log.info("SSE完成...");
            // 移除用户链接
            remove(userId);
        };
    }

    /**
     * 创建异常回调函数
     *
     * @param userId 用户ID
     * @return Consumer<Throwable>回调函数
     */
    public static Consumer<Throwable> errorCallback(String userId) {
        return Throwable -> {
            log.info("SSE异常...");
            // 移除用户链接
            remove(userId);
        };
    }

    /**
     * 移除指定用户的SSE连接
     *
     * @param userId 需要移除的用户ID
     */
    public static void remove(String userId) {
        // 删除用户
        sseClients.remove(userId);
        log.info("SSE连接被移除, 移除的用户ID为：{}", userId);
    }

}

