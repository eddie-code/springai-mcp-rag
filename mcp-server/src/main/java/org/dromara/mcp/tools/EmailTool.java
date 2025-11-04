// 包声明，定义当前类所在的包路径
package org.dromara.mcp.tools;

// 导入 flexmark 库的相关类，用于 Markdown 到 HTML 的转换
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

// 导入 Jakarta EE 中处理邮件的核心类
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

// Lombok 注解简化 JavaBean 编写
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 日志框架 Slf4j 的 Lombok 注解支持
import lombok.extern.slf4j.Slf4j;

// Spring AI 工具类相关注解，标识方法可作为 MCP 工具调用
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

// Spring 框架依赖注入相关注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

// Spring Mail 相关核心类，用于发送邮件
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

// 标识该类为 Spring 组件，便于自动扫描注册 Bean
import org.springframework.stereotype.Component;

/**
 * @author lee
 * @description 邮件工具类，提供发送邮件和获取邮箱地址的功能
 */
@Component
@Slf4j
public class EmailTool {

    // JavaMailSender 实例，用于创建和发送邮件
    private final JavaMailSender mailSender;
    
    // 发送方邮箱地址，从配置文件读取
    private final String from;

    // 构造函数注入 JavaMailSender 和发件人地址
    @Autowired
    public EmailTool(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    // 使用 Lombok 提供的数据操作注解（getter/setter/toString等）
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    // 定义一个静态内部类 EmailRequest，封装邮件请求参数
    public static class EmailRequest{
        
        // 收件人邮箱地址字段，并标注其用途描述
        @ToolParam(description = "收件人邮箱")
        private String email;

        // 邮件主题字段，并标注其用途描述
        @ToolParam(description = "发送邮件的标题/主题")
        private String subjects;

        // 邮件正文内容字段，并标注其用途描述
        @ToolParam(description = "发送邮件的消息/正文内容")
        private String message;

        // 内容类型字段，表示是纯文本、Markdown 或 HTML 类型，并标注其用途描述
        @ToolParam(description = "邮件的内容是否为html还是markdown格式，如果是markdown格式，则为1；如果是html格式，则为2")
        private Integer contentType;
    }

    // 声明一个 MCP 工具方法，用于获取当前用户的邮箱地址
    @Tool(description = "查询我的邮件/邮箱地址")
    public String getMyEmailAddress() {
        log.info("========== 调用MCP工具：getMyEmailAddress() ==========");
        return "3074182915@qq.com"; // 返回预设邮箱地址
    }

    // 声明另一个 MCP 工具方法，用于发送邮件
    @Tool(description = "给指定邮箱发送邮件信息，email 为收件人邮箱，subject 为邮件标题，message为邮件的内容")
    public String sendMailMessage(EmailRequest emailRequest){
        log.info("========== 调用MCP工具：sendMailMessage() ===========");
        log.info("========== 参数 emailRequest: {} ===========", emailRequest.toString());

        // 参数校验：检查收件人邮箱是否为空
        if (emailRequest.getEmail() == null || emailRequest.getEmail().trim().isEmpty()) {
            return "发送失败：收件人邮箱不能为空";
        }

        // 参数校验：检查邮件主题是否为空
        if (emailRequest.getSubjects() == null || emailRequest.getSubjects().trim().isEmpty()) {
            return "发送失败：邮件主题不能为空";
        }

        // 参数校验：检查邮件正文是否为空
        if (emailRequest.getMessage() == null || emailRequest.getMessage().trim().isEmpty()) {
            return "发送失败：邮件内容不能为空";
        }

        // 获取并设置默认内容类型为纯文本（0）
        Integer contentType = emailRequest.getContentType();
        if (contentType == null) {
            contentType = 0; 
        }

        // 创建 MIME 邮件对象及辅助构建者
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // 初始化 MimeMessageHelper 并设置编码为 UTF-8
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(from);                         // 设置发件人
            mimeMessageHelper.setTo(emailRequest.getEmail().trim()); // 设置收件人
            mimeMessageHelper.setSubject(emailRequest.getSubjects().trim()); // 设置主题

            // 获取并清理邮件正文内容
            String messageContent = emailRequest.getMessage().trim();

            // 根据 contentType 处理不同类型的邮件内容
            if (contentType == 1) {
                // Markdown 格式转成 HTML 后发送
                String htmlContent = convertToHtml(messageContent);
                mimeMessageHelper.setText(htmlContent, true);
                log.debug("转换为HTML内容: {}", htmlContent);
            } else if (contentType == 2) {
                // 直接以 HTML 格式发送
                mimeMessageHelper.setText(messageContent, true);
            } else {
                // 默认以纯文本格式发送
                mimeMessageHelper.setText(messageContent, false);
            }

            // 执行实际的邮件发送动作
            mailSender.send(mimeMessage);
            
            // 成功后记录日志并返回结果提示
            String successMsg = String.format("邮件已成功发送至 %s", emailRequest.getEmail());
            log.info("========== {} ===========", successMsg);
            return successMsg;

        } catch (MessagingException e) { // 捕获邮件协议层面的异常
            String errorMsg = String.format("发送邮件失败: %s", e.getMessage());
            log.error("========== {} ===========", errorMsg, e);
            return errorMsg;
        } catch (Exception e) { // 兜底捕获其他运行时异常
            String errorMsg = String.format("发送邮件时发生未知错误: %s", e.getMessage());
            log.error("========== {} ===========", errorMsg, e);
            return errorMsg;
        }
    }

    /**
     * 将 Markdown 字符串转换为 HTML
     * @param markdownStr Markdown 格式的字符串
     * @return 转换后的 HTML 字符串
     */
    public static String convertToHtml(String markdownStr) {
        try {
            // 创建解析选项配置
            MutableDataSet dataSet = new MutableDataSet();
            // 构建 Markdown 解析器
            Parser parser = Parser.builder(dataSet).build();
            // 构建 HTML 渲染器
            HtmlRenderer htmlRenderer = HtmlRenderer.builder(dataSet).build();
            // 执行解析与渲染过程
            return htmlRenderer.render(parser.parse(markdownStr));
        } catch (Exception e) {
            // 出现异常则记录日志，并返回原始文本包装成 HTML pre 标签形式
            log.error("Markdown转换HTML失败: {}", e.getMessage());
            return "<pre>" + markdownStr + "</pre>";
        }
    }
}
