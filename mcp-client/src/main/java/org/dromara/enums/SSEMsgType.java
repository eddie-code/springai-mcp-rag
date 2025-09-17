package org.dromara.enums;

/**
 * 发送SSE消息类型
 *
 * @author lee
 * @description
 */
public enum SSEMsgType {

    MESSAGE("message", "单词发送的普通类型"),
    ADD("add", "消息追加, 适合于流失stream推送"),
    FINISH("finish", "消息完成"),
    CUSTOM_EVENT("custom_event", "自定义事件"),
    DONE("done", "完成");

    public final String type;
    public final String value;

    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
