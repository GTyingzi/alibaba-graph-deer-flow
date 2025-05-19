package com.alibaba.cloud.ai.example.graph.config;

import com.alibaba.cloud.ai.example.graph.dispatcher.CoordinatorDispatcher;
import com.alibaba.cloud.ai.example.graph.dispatcher.HumanFeedbackDispatcher;
import com.alibaba.cloud.ai.example.graph.dispatcher.PlannerDispatcher;
import com.alibaba.cloud.ai.example.graph.dispatcher.ResearchTeamDispatcher;
import com.alibaba.cloud.ai.example.graph.node.*;
import com.alibaba.cloud.ai.example.graph.tool.search.tavily.TavilySearchApi;
import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * @author yingzi
 * @date 2025/5/17 17:10
 */
@Configuration
public class DeepResearchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DeepResearchConfiguration.class);

    @Autowired
    private TavilySearchApi tavilySearchApi;

    @Bean
    public StateGraph deepResearch(ChatModel chatModel) throws GraphStateException {
        ChatClient chatClient = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor()).build();
        OverAllStateFactory stateFactory = () -> {
            OverAllState state = new OverAllState();
            state.registerKeyAndStrategy("coordinator_next_node", new ReplaceStrategy());
            state.registerKeyAndStrategy("planner_next_node", new ReplaceStrategy());
            state.registerKeyAndStrategy("human_next_node", new ReplaceStrategy());
            state.registerKeyAndStrategy("research_team_next_node", new ReplaceStrategy());

            state.registerKeyAndStrategy("messages", new ReplaceStrategy());
            state.registerKeyAndStrategy("background_investigation_results", new ReplaceStrategy());
            state.registerKeyAndStrategy("enable_background_investigation", new ReplaceStrategy());
            state.registerKeyAndStrategy("plan_iterations", new ReplaceStrategy());
            state.registerKeyAndStrategy("current_plan", new ReplaceStrategy());
            state.registerKeyAndStrategy("auto_accepted_plan", new ReplaceStrategy());
            state.registerKeyAndStrategy("observations", new ReplaceStrategy());
            state.registerKeyAndStrategy("final_report", new ReplaceStrategy());
            return state;
        };

        StateGraph stateGraph = new StateGraph("deep research", stateFactory)
                .addNode("coordinator", node_async(new CoordinatorNode(chatClient)))
                .addNode("background_investigator", node_async((new BackgroundInvestigationNode(tavilySearchApi))))
                .addNode("planner", node_async((new PlannerNode(chatClient))))
                .addNode("human_feedback", node_async(new HumanFeedbackNode()))
                .addNode("research_team", node_async(new ResearchTeamNode()))
                .addNode("researcher", node_async(new ResearcherNode(chatClient)))
                .addNode("coder", node_async(new CoderNode()))
                .addNode("reporter", node_async((new ReporterNode(chatClient))))

                .addEdge(START, "coordinator")
                .addConditionalEdges("coordinator", edge_async(new CoordinatorDispatcher()),
                        Map.of("background_investigator", "background_investigator", END, END)
                )
                .addConditionalEdges("planner", edge_async(new PlannerDispatcher()),
                        Map.of("reporter", "reporter", "human_feedback", "human_feedback", END, END)
                )
                .addConditionalEdges("human_feedback", edge_async(new HumanFeedbackDispatcher()),
                        Map.of("planner", "planner", "research_team", "research_team", "reporter", "reporter", END, END)
                )
                .addConditionalEdges("research_team", edge_async(new ResearchTeamDispatcher()),
                        Map.of("planner", "planner", "researcher", "researcher", "coder", "coder")
                )
                .addEdge("researcher", "research_team")
                .addEdge("coder", "research_team")
                .addEdge("reporter", END);

        GraphRepresentation graphRepresentation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "workflow graph");

        logger.info("\n\n");
        logger.info(graphRepresentation.content());
        logger.info("\n\n");

        return stateGraph;
    }
}
