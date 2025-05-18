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


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(String searchDepth) {
        this.searchDepth = searchDepth;
    }

    public int getChunksPerSource() {
        return chunksPerSource;
    }

    public void setChunksPerSource(int chunksPerSource) {
        this.chunksPerSource = chunksPerSource;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public boolean isIncludeAnswer() {
        return includeAnswer;
    }

    public void setIncludeAnswer(boolean includeAnswer) {
        this.includeAnswer = includeAnswer;
    }

    public boolean isIncludeRawContent() {
        return includeRawContent;
    }

    public void setIncludeRawContent(boolean includeRawContent) {
        this.includeRawContent = includeRawContent;
    }

    public boolean isIncludeImages() {
        return includeImages;
    }

    public void setIncludeImages(boolean includeImages) {
        this.includeImages = includeImages;
    }

    public boolean isIncludeImageDescriptions() {
        return includeImageDescriptions;
    }

    public void setIncludeImageDescriptions(boolean includeImageDescriptions) {
        this.includeImageDescriptions = includeImageDescriptions;
    }

    public List<String> getIncludeDomains() {
        return includeDomains;
    }

    public void setIncludeDomains(List<String> includeDomains) {
        this.includeDomains = includeDomains;
    }

    public List<String> getExcludeDomains() {
        return excludeDomains;
    }

    public void setExcludeDomains(List<String> excludeDomains) {
        this.excludeDomains = excludeDomains;
    }

}
