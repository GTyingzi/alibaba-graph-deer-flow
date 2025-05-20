package com.alibaba.cloud.ai.example.graph.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphStateException;
import com.alibaba.cloud.ai.graph.StateGraph;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Map<String, Object> chat(@RequestParam(value = "query", defaultValue = "草莓蛋糕怎么做呀") String query,
                                    @RequestParam(value = "enable_background_investigation", defaultValue = "true") boolean  enableBackgroundInvestigation,
                                    @RequestParam(value = "enable_background_planning", defaultValue = "true") boolean  enableBackgroundPlanning
                                    ) {
        UserMessage userMessage = new UserMessage(query);
        var resultFuture = compiledGraph.invoke(Map.of(
                "enable_background_investigation", enableBackgroundInvestigation,
                "auto_accepted_plan", enableBackgroundPlanning,
                "messages", List.of(userMessage)));
        var result = resultFuture.get();
        return result.data();
    }
}
