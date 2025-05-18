package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.dispatcher.PlannerDispatcher;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/18 15:58
 */

public class ReporterNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(ReporterNode.class);

    private final ChatClient chatClient;

    public ReporterNode(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> apply(OverAllState t) throws Exception {
        return Map.of();
    }
}
