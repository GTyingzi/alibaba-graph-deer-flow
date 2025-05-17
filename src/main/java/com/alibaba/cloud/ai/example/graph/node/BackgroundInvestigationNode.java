package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.SearchedContent;
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


    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("background investigation node is running.");

        Optional<Object> messagesOpt = state.value("messages");
        List<Message> messages = messagesOpt
                .map(obj -> (List<Message>) obj)
                .orElse(List.of());
        Message lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        String query = lastMessage.getText();
        // todo 调用tavily_search_tool工具查询结果
        List<SearchedContent> searchedContentList = Collections.singletonList(new SearchedContent("1", "1"));
        ArrayList<SearchedContent> results = new ArrayList<>();
        for (SearchedContent searchedContent : searchedContentList) {
            results.add(
                    new SearchedContent(
                            searchedContent.title(),
                            searchedContent.content()
                    )
            );
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("background_investigation_results", results);
        return resultMap;
    }
}
