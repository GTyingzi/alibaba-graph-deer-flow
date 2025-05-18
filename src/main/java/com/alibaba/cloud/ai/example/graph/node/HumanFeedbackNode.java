package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author yingzi
 * @date 2025/5/18 16:54
 */

public class HumanFeedbackNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(PlannerNode.class);


    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("HumanFeedback node is running.");
        String nextStep = "";
        Map<String, Object> updated = new HashMap<>();

        boolean autoAcceptedPlan = state.value("auto_accepted_plan", false);
        if (!autoAcceptedPlan) {
            //
            Scanner scanner = new Scanner(System.in);
            String feedback = scanner.next();
            if (StringUtils.hasLength(feedback) && feedback.startsWith("[EDIT_PLAN]")) {
                nextStep = "planner";
                updated.put("human_next_node", nextStep);
                updated.put("messages", List.of(new UserMessage(feedback)));
                return updated;
            } else if(StringUtils.hasLength(feedback) && feedback.startsWith("[ACCEPT_PLAN]")) {
                logger.info("Plan is accepted by user.");
            } else {
                throw new Exception(String.format("Interrupt value of %s is not supported", feedback));
            }
        }

        Integer planIterations = state.value("plan_iterations", 0);
        Plan currentPlan = state.value("current_plan", Plan.class).get();
        planIterations += 1;
        if (currentPlan.isHasEnoughContext()) {
            nextStep = "reporter";
        }

        updated.put("plan_iterations", planIterations);
        updated.put("human_next_node", nextStep);
        return updated;
    }
}
