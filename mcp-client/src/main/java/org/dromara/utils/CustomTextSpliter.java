package org.dromara.utils;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

/**
 * 自定义文本分割器类
 * 继承自TextSplitter，用于将文本按照特定规则进行分割
 *
 * @author lee
 * @description
 */
public class CustomTextSpliter extends TextSplitter {

//    @Override
//    protected List<String> splitText(String text) {
//        // (?=\RQ：)  正向预查：遇到"Q："就切，但保留"Q："
//        return List.of(text.split("(?=\\RQ：)"));
//    }

    /**
     * 分割文本方法
     * 将输入的文本按照指定规则分割成字符串列表
     *
     * @param text 待分割的文本字符串
     * @return 分割后的字符串列表
     */
    @Override
    protected List<String> splitText(String text) {
        return List.of(split(text));
    }

    /**
     * 文本分割方法
     * 使用正则表达式将文本按空白字符和换行符进行分割
     *
     * @param text 待分割的文本字符串
     * @return 分割后的字符串数组
     */
    public String[] split(String text) {
        // 按照零个或多个空白字符、换行符、再零个或多个空白字符的模式进行分割
        return text.split("\\s*\\R\\s*\\R\\s*");
    }

}
