package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.*;

/**
 * @author yingzi
 * @date 2025/5/18 17:07
 */

public class CoderNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(CoderNode.class);

    private final ChatClient coderAgent;

    public CoderNode(ChatClient coderAgent) {
        this.coderAgent = coderAgent;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        Plan currentPlan = state.value("current_plan", Plan.class).get();
        List<String> observations = state.value("observations", List.class)
                .map(list -> (List<String>) list)
                .orElse(Collections.emptyList());

        Plan.Step unexecutedStep = null;
        for (Plan.Step step : currentPlan.getSteps()){
            if (step.getExecutionRes() == null){
                unexecutedStep = step;
                break;
            }
        }

        List<Message> messages = new ArrayList<>();

        // 添加任务消息
        Message taskMessage = new UserMessage(String.format("#Task\n\n##title\n\n%s\n\n##description\n\n%s\n\n##locale\n\n%s",
                unexecutedStep.getTitle(),
                unexecutedStep.getDescription(),
                state.value("locale", "en-US")));
        messages.add(taskMessage);


        // 调用agent
        String content = coderAgent.prompt()
                .messages(messages)
                .call()
                .content();
        unexecutedStep.setExecutionRes(content);

        Map<String, Object> updated = new HashMap<>();
        updated.put("messages", new AssistantMessage(content));
        updated.put("observations",
                observations.add(content)
        );

        return updated;
    }
}
