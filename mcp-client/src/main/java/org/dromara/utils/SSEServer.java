package org.dromara.utils;

import lombok.extern.slf4j.Slf4j;
import org.dromara.enums.SSEMsgType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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
            log.error("SSE异常...");
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

    /**
     * 发送SSE消息给指定的客户端
     *
     * @param sseEmitter SSE发射器实例，用于向客户端发送事件
     * @param userId 用户唯一标识符，作为事件ID
     * @param message 要发送的消息内容
     * @param msgType 消息类型，决定事件名称
     */
    private static void sendEmitterMessage(SseEmitter sseEmitter, String userId, String message, SSEMsgType msgType) {
        try {
            // 构建SSE事件消息
            SseEmitter.SseEventBuilder msgEvent = SseEmitter.event()
                    .id(userId)
                    .data(message)
                    .name(msgType.type);
            sseEmitter.send(msgEvent);
        } catch (IOException e) {
            log.error("SSE发送消息异常：{}", e.getMessage());
            remove(userId);
        }
    }

    /**
     * 向指定用户发送消息
     *
     * @param userId 目标用户的唯一标识符
     * @param message 要发送的消息内容
     * @param msgType 消息类型
     */
    public static void sendMsg(String userId, String message, SSEMsgType msgType) {
        // 检查客户端集合是否为空
        if (CollectionUtils.isEmpty(sseClients)) {
            return;
        }
        // 检查目标用户是否存在连接
        if (sseClients.containsKey(userId)) {
            SseEmitter sseEmitter = (SseEmitter) sseClients.get(userId);
            sendEmitterMessage(sseEmitter, userId, message, msgType);
        }
    }

}

