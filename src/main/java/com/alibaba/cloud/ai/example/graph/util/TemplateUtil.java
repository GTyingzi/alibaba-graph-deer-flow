package com.alibaba.cloud.ai.example.graph.util;

import com.alibaba.cloud.ai.graph.OverAllState;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author yingzi
 * @date 2025/5/17 17:20
 */

public class TemplateUtil {

    public static List<Message> applyPromptTemplate(String promptName, OverAllState state) throws IOException {
        Message systemMessage = getMessage(promptName);
        List<Message> messages = getMessages(state);

        messages.add(0, systemMessage);

        return messages;
    }

    public static Message getMessage(String promptName) throws IOException {
        // 读取 resources/prompts 下的 md 文件
        ClassPathResource resource = new ClassPathResource("prompts/" + promptName + ".md");
        String template = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        // 替换 {{ CURRENT_TIME }} 占位符
        String systemPrompt = template.replace("{{ CURRENT_TIME }}", LocalDateTime.now().toString());
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        return systemMessage;
    }

    public static List<Message> getMessages(OverAllState state) {
        Optional<Object> messagesOpt = state.value("messages");
        List<Message> messages = messagesOpt
                .map(obj -> (List<Message>) obj)
                .orElse(List.of());
        return messages;
    }

}
