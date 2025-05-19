package com.alibaba.cloud.ai.example.graph.agents;


import com.alibaba.cloud.ai.example.graph.tool.PythonReplTool;
import com.alibaba.cloud.ai.example.graph.tool.WebSearchTool;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.SneakyThrows;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgentsConfiguration {


    @Autowired
    private List<McpSyncClient> mcpSyncClients;  // For sync client
    @Autowired
    private PythonReplTool pythonReplTool;
    @Autowired
    private WebSearchTool webSearchTool;

    @Value("classpath:prompts/researcher.md")
    private Resource researcherPrompt;

    @Value("classpath:prompts/coder.md")
    private Resource coderPrompt;


    @SneakyThrows
    @Bean
    public ChatClient researchAgent(ChatClient.Builder chatClientBuilder) {
        List<McpSchema.Tool> mcpTools = new ArrayList<>();

        if (CollectionUtils.isEmpty(mcpSyncClients)) {
            for (McpSyncClient client : mcpSyncClients) {
                McpSchema.ListToolsResult listToolsResult = client.listTools();
                mcpTools.addAll(listToolsResult.tools());
            }
        }else {
            MethodToolCallbackProvider build = MethodToolCallbackProvider.builder().toolObjects(webSearchTool).build();
            chatClientBuilder.defaultToolCallbacks(
                    build.getToolCallbacks()
            ).build();
        }

        return chatClientBuilder
                .defaultTools(mcpTools)
                .defaultSystem(researcherPrompt.getContentAsString(Charset.defaultCharset()))
                .build();
    }


    @SneakyThrows
    @Bean
    public ChatClient coderAgent(ChatClient.Builder chatClientBuilder) {
        List<McpSchema.Tool> mcpTools = new ArrayList<>();

        if (CollectionUtils.isEmpty(mcpSyncClients)) {
            for (McpSyncClient client : mcpSyncClients) {
                McpSchema.ListToolsResult listToolsResult = client.listTools();
                mcpTools.addAll(listToolsResult.tools());
            }
            chatClientBuilder.defaultTools(mcpTools);
        } else {
            MethodToolCallbackProvider build = MethodToolCallbackProvider.builder().toolObjects(pythonReplTool).build();
            chatClientBuilder.defaultToolCallbacks(
                    build.getToolCallbacks()
            ).build();
        }

        return chatClientBuilder
                .defaultSystem(coderPrompt.getContentAsString(Charset.defaultCharset()))
                .build();
    }
}
