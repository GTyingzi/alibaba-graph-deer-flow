package com.alibaba.cloud.ai.example.graph.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/18 14:52
 */
@Data
@Builder
public class TavilySearchRequest {

    @JsonProperty("query")
    private String query;
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("search_depth")
    private String searchDepth;
    @JsonProperty("chunks_per_source")
    private int chunksPerSource;
    @JsonProperty("max_results")
    private int maxResults;
    @JsonProperty("time_range")
    private String timeRange;
    @JsonProperty("days")
    private int days;
    @JsonProperty("include_answer")
    private boolean includeAnswer;
    @JsonProperty("include_raw_content")
    private boolean includeRawContent;
    @JsonProperty("include_images")
    private boolean includeImages;
    @JsonProperty("include_image_descriptions")
    private boolean includeImageDescriptions;
    @JsonProperty("include_domains")
    private List<String> includeDomains;
    @JsonProperty("exclude_domains")
    private List<String> excludeDomains;

}
