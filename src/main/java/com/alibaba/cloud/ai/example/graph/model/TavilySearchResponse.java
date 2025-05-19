package com.alibaba.cloud.ai.example.graph.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author yingzi
 * @date 2025/5/18 15:01
 */

public class TavilySearchResponse {

    private String query;
    private String answer;
    private List<ImageInfo> images;
    private List<ResultInfo> results;
    @JsonProperty("response_time")
    private double responseTime;


    public class ImageInfo {
        private String url;
        private String description;

    }

    public class ResultInfo {
        private String title;
        private String url;
        private String content;
        private double score;
        @JsonProperty("raw_content")
        private String rawContent;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }

    public List<ResultInfo> getResults() {
        return results;
    }

    public void setResults(List<ResultInfo> results) {
        this.results = results;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }
}
