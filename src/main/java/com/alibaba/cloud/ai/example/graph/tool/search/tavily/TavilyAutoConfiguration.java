package com.alibaba.cloud.ai.example.graph.tool.search.tavily;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @date 2025/5/18 15:26
 */
@Configuration
@EnableConfigurationProperties(TavilySearchProperties.class)
public class TavilyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TavilySearchApi tavilySearchApi(TavilySearchProperties properties) {
        return new TavilySearchApi(properties);
    }
}
