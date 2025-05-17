package com.alibaba.cloud.ai.example.graph.config;

import com.alibaba.cloud.ai.example.graph.dispatcher.CoordinatorDispatcher;
import com.alibaba.cloud.ai.example.graph.node.BackgroundInvestigationNode;
import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
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

    @Bean
    public StateGraph deepResearch(ChatModel chatModel) throws GraphStateException {
        ChatClient chatClient = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor()).build();


        OverAllStateFactory stateFactory = () -> {
            OverAllState state = new OverAllState();
            state.registerKeyAndStrategy("messages", new ReplaceStrategy());
            return state;
        };


        StateGraph stateGraph = new StateGraph("deep research", stateFactory)
                .addEdge(START, "coordinator")
                .addConditionalEdges("coordinator", edge_async(new CoordinatorDispatcher(chatClient)),
                        Map.of("background_investigator", "background_investigator", END, END)
                )
                .addNode("background_investigator", node_async((new BackgroundInvestigationNode())))
//                .addEdge("background_investigator", "planner")
                .addEdge("background_investigator", END)
                ;

        GraphRepresentation graphRepresentation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "workflow graph");

        logger.info("\n\n");
        logger.info(graphRepresentation.content());
        logger.info("\n\n");

        return stateGraph;
    }
}
