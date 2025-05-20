package com.alibaba.cloud.ai.example.graph.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.GraphStateException;
import com.alibaba.cloud.ai.graph.state.StateSnapshot;
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
                                    @RequestParam(value = "auto_accepted_plan", defaultValue = "true") boolean  autoAcceptedPlan,
                                    @RequestParam(value = "thread_id", required = false) int threadId
                                    ) {
        UserMessage userMessage = new UserMessage(query);
        Map<String, Object> objectMap = Map.of(
                "enable_background_investigation", enableBackgroundInvestigation,
                "auto_accepted_plan", autoAcceptedPlan,
                "messages", List.of(userMessage));

        if (threadId != 0) {
            RunnableConfig runnableConfig = RunnableConfig.builder().threadId(String.valueOf(threadId)).build();
            var resultFuture = compiledGraph.invoke(objectMap, runnableConfig);
            return resultFuture.get().data();
        } else {
            var resultFuture = compiledGraph.invoke(objectMap);
            return resultFuture.get().data();
        }
    }

    @GetMapping("/chat/resume")
    public Map<String, Object> resume(@RequestParam(value = "thread_id", required = true) int threadId) {
        RunnableConfig runnableConfig = RunnableConfig.builder().threadId(String.valueOf(threadId)).build();
        Map<String, Object> objectMap = Map.of("feed_back", "y");

        StateSnapshot stateSnapshot = compiledGraph.getState(runnableConfig);
        OverAllState state = stateSnapshot.state();
        state.withResume();
        state.withHumanFeedback(new OverAllState.HumanFeedback(objectMap, "research_team"));

        var resultFuture = compiledGraph.invoke(objectMap, runnableConfig);

        return resultFuture.get().data();
    }
}
