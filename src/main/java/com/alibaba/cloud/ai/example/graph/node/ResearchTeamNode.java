package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/18 16:59
 */

public class ResearchTeamNode implements NodeAction {
    @Override
    public Map<String, Object> apply(OverAllState t) throws Exception {
        String nextStep = "";
        Map<String, Object> updated = new HashMap<>();
        updated.put("research_team_next_node", nextStep);
        return updated;    }
}
