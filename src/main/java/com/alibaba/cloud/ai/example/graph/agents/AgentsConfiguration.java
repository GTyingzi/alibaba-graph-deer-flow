package com.alibaba.cloud.ai.example.graph.agents;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgentsConfiguration {


    @Autowired
    private List<McpSyncClient> mcpSyncClients;  // For sync client

    @Value("classpath:prompts/researcher.md")
    private Resource researcherPrompt;

    @SneakyThrows
    @Bean
    public ChatClient researchAgent(ChatClient.Builder chatClientBuilder) {
        List<McpSchema.Tool> mcpTools = new ArrayList<>();

        for (McpSyncClient client : mcpSyncClients) {
            McpSchema.ListToolsResult listToolsResult = client.listTools();
            mcpTools.addAll(listToolsResult.tools());
        }

        return chatClientBuilder
                .defaultTools(mcpTools)
                .defaultSystem(researcherPrompt.getContentAsString(Charset.defaultCharset()))
                .build();
    }
}
