package com.alibaba.cloud.ai.example.graph.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphStateException;
import com.alibaba.cloud.ai.graph.StateGraph;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/17 19:27
 */
@RestController
@RequestMapping("/deep-research")
public class DeepResearchController {

    private final CompiledGraph compiledGraph;

    @Autowired
    public DeepResearchController(@Qualifier("deepResearch") StateGraph stateGraph)
            throws GraphStateException {
        this.compiledGraph = stateGraph.compile();
    }

    @GetMapping("/chat")
    public Map<String, Object> chat(String prompt) {
        UserMessage userMessage = new UserMessage(prompt);
        var resultFuture = compiledGraph.invoke(Map.of("messages", userMessage));
        var result = resultFuture.get();
        return result.data();
    }
}
