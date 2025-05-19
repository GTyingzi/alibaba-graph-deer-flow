package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.SearchedContent;
import com.alibaba.cloud.ai.example.graph.model.TavilySearchResponse;
import com.alibaba.cloud.ai.example.graph.tool.search.tavily.TavilySearchApi;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;

import java.util.*;

/**
 * @author yingzi
 * @date 2025/5/17 18:37
 */

public class BackgroundInvestigationNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(BackgroundInvestigationNode.class);

    private final TavilySearchApi tavilySearchApi;
    public BackgroundInvestigationNode(TavilySearchApi tavilySearchApi){
        this.tavilySearchApi = tavilySearchApi;
    }


    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("background investigation node is running.");
        List<Message> messages = state.value("messages", List.class)
                .map(obj -> new ArrayList<>((List<Message>) obj))
                .orElseGet(ArrayList::new);
        Message lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        String query = lastMessage.getText();
        TavilySearchResponse response = tavilySearchApi.search(query);
        ArrayList<SearchedContent> results = new ArrayList<>();
        for (TavilySearchResponse.ResultInfo resultInfo: response.getResults()) {
            results.add(
                    new SearchedContent(
                            resultInfo.getTitle(),
                            resultInfo.getContent()
                    )
            );
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("background_investigation_results", results);
        return resultMap;
    }
}
