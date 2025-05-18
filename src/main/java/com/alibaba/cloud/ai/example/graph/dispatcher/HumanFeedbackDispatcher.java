package com.alibaba.cloud.ai.example.graph.dispatcher;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import static com.alibaba.cloud.ai.graph.StateGraph.END;

/**
 * @author yingzi
 * @date 2025/5/18 16:01
 */

public class HumanFeedbackDispatcher implements EdgeAction {

    private static final Logger logger = LoggerFactory.getLogger(HumanFeedbackDispatcher.class);

    private final ChatClient chatClient;

    public HumanFeedbackDispatcher(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String apply(OverAllState state) throws Exception {
        return (String) state.value("human_next_step").orElse(END);
    }
}
