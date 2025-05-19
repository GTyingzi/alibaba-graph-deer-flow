package com.alibaba.cloud.ai.example.graph.dispatcher;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;

import static com.alibaba.cloud.ai.graph.StateGraph.END;


/**
 * @author yingzi
 * @date 2025/5/18 15:52
 */

public class PlannerDispatcher implements EdgeAction {

    @Override
    public String apply(OverAllState state) {
        return (String) state.value("planner_next_node", END);
    }
}
