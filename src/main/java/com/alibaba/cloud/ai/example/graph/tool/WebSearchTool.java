package com.alibaba.cloud.ai.example.graph.tool;


import com.alibaba.cloud.ai.example.graph.model.TavilySearchResponse;
import com.alibaba.cloud.ai.example.graph.tool.search.tavily.TavilySearchApi;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSearchTool {

    @Autowired
    private TavilySearchApi tavilySearchApi;

    @Tool(description = "Search the web for information")
    public String search(@ToolParam(description = "search content") String query) {
        TavilySearchResponse search = tavilySearchApi.search(query);
        return search.getResults().get(0).getContent();
    }

}
