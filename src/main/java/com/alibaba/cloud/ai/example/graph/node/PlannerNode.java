package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.example.graph.util.TemplateUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @date 2025/5/18 16:47
 */

public class PlannerNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(PlannerNode.class);

    private final ChatClient chatClient;
    private final BeanOutputConverter<Plan> converter;


    public PlannerNode(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.converter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<Plan>() {
                }
        );
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("Planner node is running.");
        List<Message> messages = TemplateUtil.applyPromptTemplate("planner", state);
        Integer planIterations = state.value("plan_iterations", 0);
        Boolean enableBackgroundInvestigation = state.value("enable_background_investigation", false);
        ArrayList<String> backgroundInvestigationResults = state.value("background_investigation_results", new ArrayList<>());

        if (planIterations == 0 && enableBackgroundInvestigation && !backgroundInvestigationResults.isEmpty()) {
            messages.add(
                    SystemMessage.builder()
                            .text(
                                    "background investigation results of user query:\n"
                                            + backgroundInvestigationResults
                                            + "\n"
                            )
                            .build()
            );
        }
        String nextStep = "reporter";
        Map<String, Object> updated = new HashMap<>();
        if (planIterations > 3) {
            updated.put("planner_next_node", nextStep);
            return updated;
        }

        String promptUserSpec = """
                format: 以纯文本输出 json，请不要包含任何多余的文字——包括 markdown 格式;
                outputExample: $format$;
                """;

        String result = chatClient.prompt()
                .user(u -> u.text(promptUserSpec)
                        .param("format", converter.getFormat()))
                .messages(messages)
                .call()
                .content();
        logger.info("Planner response: {}", result);
        assert result != null;
        try {
            Plan curPlan = converter.convert(result);
            logger.info("反序列成功，convert: {}", curPlan);
            if (curPlan.isHasEnoughContext()) {
                logger.info("Planner response has enough context.");
                updated.put("current_plan", curPlan);
                updated.put("messages", new AssistantMessage(result));
                updated.put("planner_next_node", nextStep);
                return updated;
            }
        } catch (Exception e) {
            logger.error("反序列化失败");
            if (planIterations > 0) {
                updated.put("planner_next_node", nextStep);
                return updated;
            } else {
                nextStep = END;
                updated.put("planner_next_node", nextStep);
                return updated;
            }
        }

        nextStep = "human_feedback";
        updated.put("current_plan", result);
        updated.put("messages", new AssistantMessage(result));
        updated.put("planner_next_node", nextStep);

        return updated;
    }
}
