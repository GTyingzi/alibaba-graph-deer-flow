package com.alibaba.cloud.ai.example.graph.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/18 15:01
 */
@Data
public class TavilySearchResponse {

    private String query;
    private String answer;
    private List<ImageInfo> images;
    private List<ResultInfo> results;
    @JsonProperty("response_time")
    private double responseTime;

    @Data
    public static class ImageInfo {
        private String url;
        private String description;

    }

    @Data
    public static class ResultInfo {
        private String title;
        private String url;
        private String content;
        private double score;
        @JsonProperty("raw_content")
        private String rawContent;
    }

}
