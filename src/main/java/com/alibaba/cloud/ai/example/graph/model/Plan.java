package com.alibaba.cloud.ai.example.graph.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/18 17:48
 */
@Data
public class Plan {

    private String title;
    @JsonProperty("has_enough_context")
    private boolean hasEnoughContext;
    private String thought;
    private List<Step> steps;

    @Data
    public static class Step {
        @JsonProperty("need_web_search")
        private boolean needWebSearch;
        private String title;
        private String description;
        @JsonProperty("step_type")
        private StepType stepType;
        private String executionRes;
    }

    public enum StepType {
        @JsonProperty("research")
        RESEARCH,
        @JsonProperty("processing")
        PROCESSING
    }

    public boolean isHasEnoughContext() {
        return hasEnoughContext;
    }


}
