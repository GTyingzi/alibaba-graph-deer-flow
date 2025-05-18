package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/18 16:47
 */

public class PlannerNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(PlannerNode.class);

    private final ChatClient chatClient;

    public PlannerNode(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> apply(OverAllState t) throws Exception {
        String nextStep = "";
        Map<String, Object> updated = new HashMap<>();
        updated.put("planner_next_node", nextStep);
        return updated;
    }
}
