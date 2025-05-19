package com.alibaba.cloud.ai.example.graph.tool;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @author yingzi
 * @date 2025/5/17 18:10
 */

public class PlannerTool {

    @Tool(name = "handoff_to_planner", description = "Handoff to planner agent to do plan.")
    public void handoffToPlanner(String taskTitle) {
        // This method is not returning anything. It is used as a way for LLM
        // to signal that it needs to hand off to the planner agent.
    }}
