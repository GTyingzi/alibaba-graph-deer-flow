package com.alibaba.cloud.ai.example.graph.dispatcher;

import com.alibaba.cloud.ai.example.graph.tool.PlannerTool;
import com.alibaba.cloud.ai.example.graph.util.TemplateUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @date 2025/5/17 18:31
 */

public class CoordinatorDispatcher implements EdgeAction {

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorDispatcher.class);

    private final ChatClient chatClient;

    public CoordinatorDispatcher(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String apply(OverAllState state) throws Exception {
        logger.info("Coordinator talking.");
        List<Message> messages = TemplateUtil.applyPromptTemplate("coordinator", state);
        logger.debug("Current Coordinator messages: {}", messages);

        // 发起调用并获取完整响应
        ChatResponse response = chatClient.prompt()
                .messages(messages)
                .tools(new PlannerTool())
                .call().chatResponse();

        String nextStep = END;

        // 获取 assistant 消息内容
        assert response != null;
        AssistantMessage assistantMessage = response.getResult().getOutput();

        // 判断是否触发工具调用
        if (assistantMessage.getToolCalls() != null && !assistantMessage.getToolCalls().isEmpty()) {
            logger.info("✅ 工具已调用: " + assistantMessage.getToolCalls());
            if ((boolean) state.value("enable_background_investigation").get()) {
                nextStep = "background_investigator";
            }
            for (AssistantMessage.ToolCall toolCall : assistantMessage.getToolCalls()) {
                if (!"handoff_to_planner".equals(toolCall.name())) {
                    continue;
                }
            }

        } else {
            logger.warn("❌ 未触发工具调用");
            logger.debug("Coordinator response: {}", response.getResult());
        }
        return nextStep;
    }
}
