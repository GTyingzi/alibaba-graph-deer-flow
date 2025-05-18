package com.alibaba.cloud.ai.example.graph.model;

import lombok.Data;

import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/18 17:48
 */
@Data
public class Plan {

    private String title;
    private boolean hasEnoughContext;
    private String thought;
    private List<Step> steps;

    @Data
    public class Step {

        private boolean needWebSearch;
        private String title;
        private String description;
        private StepType stepType;
        private String executionRes;
    }

    public enum StepType {
        RESEARCH,
        PROCESSING
    }

    public boolean isHasEnoughContext() {
        return hasEnoughContext;
    }


}
