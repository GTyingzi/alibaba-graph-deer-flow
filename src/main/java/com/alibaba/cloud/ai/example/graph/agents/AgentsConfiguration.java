package com.alibaba.cloud.ai.example.graph.agents;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgentsConfiguration {


    @Autowired
    private List<McpSyncClient> mcpSyncClients;  // For sync client

//    @Autowired
//    private List<McpAsyncClient> mcpAsyncClients;  // For async client

    @Bean
    public ChatClient researchAgent(ChatModel chatModel) {
        List<McpSchema.Tool> mcpTools = new ArrayList<>();

        for (McpSyncClient client : mcpSyncClients) {
            McpSchema.ListToolsResult listToolsResult = client.listTools();
            mcpTools.addAll(listToolsResult.tools());
        }

//        for (McpSyncClient client : mcpSyncClients) {
//            McpSchema.ListToolsResult listToolsResult = client.listTools();
//            mcpTools.addAll(listToolsResult.tools());
//        }

        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
        return chatClientBuilder
                .defaultTools(mcpTools)
                .build();
    }
}
