package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yingzi
 * @date 2025/5/18 16:59
 */

public class ResearchTeamNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(ResearchTeamNode.class);


    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("ResearchTeam node is running.");
        String nextStep = "planner";
        Map<String, Object> updated = new HashMap<>();

        Optional<Plan> currentPlanOpt = state.value("current_plan", Plan.class);
        if (currentPlanOpt.isEmpty() || !currentPlanOpt.get().getSteps().isEmpty()) {
            updated.put("research_team_next_node", nextStep);
            return updated;
        }

        Plan curPlan = currentPlanOpt.get();
        // 判断steps里的每个step是否都执行完毕
        if (areAllExecutionResultsPresent(curPlan)) {
            updated.put("research_team_next_node", nextStep);
            return updated;
        }

        for (Plan.Step step : curPlan.getSteps()) {
            if (!StringUtils.hasLength(step.getExecutionRes())) {
                if (step.getStepType() == Plan.StepType.RESEARCH) {
                    nextStep = "researcher";
                    updated.put("research_team_next_node", nextStep);
                    return updated;
                } else if (step.getStepType() == Plan.StepType.PROCESSING) {
                    nextStep = "coder";
                    updated.put("research_team_next_node", nextStep);
                    return updated;
                }
            }
        }
        updated.put("research_team_next_node", nextStep);
        return updated;
    }

    public boolean areAllExecutionResultsPresent(Plan plan) {
        if (plan.getSteps() == null || plan.getSteps().isEmpty()) {
            return false;
        }

        return plan.getSteps().stream()
                .allMatch(step -> StringUtils.hasLength(step.getExecutionRes()));
    }

}
