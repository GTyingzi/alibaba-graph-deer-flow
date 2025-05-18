package com.alibaba.cloud.ai.example.graph.tool.search.tavily;

import com.alibaba.cloud.ai.example.graph.model.TavilySearchRequest;
import com.alibaba.cloud.ai.example.graph.model.TavilySearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yingzi
 * @date 2025/5/18 14:36
 */
@Service
public class TavilySearchApi {
    private final String URL = "https://api.tavily.com/search";
    private final WebClient webClient;
    private final TavilySearchProperties properties;

    public TavilySearchApi(TavilySearchProperties properties) {
        this.webClient = WebClient.builder()
                .baseUrl(URL)
                .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                .build();
        this.properties = properties;
    }

    public TavilySearchResponse search(String query) {
        TavilySearchRequest build = TavilySearchRequest.builder()
                .query(query)
                .topic(properties.getTopic())
                .searchDepth(properties.getSearchDepth())
                .chunksPerSource(properties.getChunksPerSource())
                .maxResults(properties.getMaxResults())
                .days(properties.getDays())
                .includeRawContent(properties.isIncludeRawContent())
                .includeImages(properties.isIncludeImages())
                .includeImageDescriptions(properties.isIncludeImageDescriptions())
                .build();
        TavilySearchResponse response = webClient.post()
                .bodyValue(build)
                .retrieve()
                .bodyToMono(TavilySearchResponse.class)
                .block();
        return response;
    }

}
