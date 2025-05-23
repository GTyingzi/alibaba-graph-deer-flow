package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import io.modelcontextprotocol.client.McpSyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.*;


public class ResearcherNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(ResearcherNode.class);

    private final ChatClient researchAgent;

    public ResearcherNode(ChatClient researchAgent) {
        this.researchAgent = researchAgent;
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

        // 添加研究者特有的引用提醒
        Message citationMessage = new SystemMessage(
                "IMPORTANT: DO NOT include inline citations in the text. Instead, track all sources and include a References section at the end using link reference format. Include an empty line between each citation for better readability. Use this format for each reference:\n- [Source Title](URL)\n\n- [Another Source](URL)"
        );
        messages.add(citationMessage);

        // 调用agent
        String content = researchAgent.prompt()
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
