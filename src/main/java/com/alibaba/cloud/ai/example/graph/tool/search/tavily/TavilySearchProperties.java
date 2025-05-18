package com.alibaba.cloud.ai.example.graph.tool.search.tavily;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yingzi
 * @date 2025/5/18 15:24
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.ai.tavily")
public class TavilySearchProperties {

    private String apiKey;

    private String topic = "general";

    private String searchDepth = "basic";

    private int maxResults = 5;

    private int chunksPerSource = 3;

    private int days = 7;

    private boolean includeRawContent = false;

    private boolean includeImages = false;

    private boolean includeImageDescriptions = false;

}
