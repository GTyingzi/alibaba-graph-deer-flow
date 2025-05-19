package com.alibaba.cloud.ai.example.graph.dispatcher;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;


/**
 * @author yingzi
 * @date 2025/5/18 16:10
 */

public class ResearchTeamDispatcher implements EdgeAction {
    @Override
    public String apply(OverAllState state) throws Exception {
        return (String) state.value("research_team_next_node", "planner");
    }
}
