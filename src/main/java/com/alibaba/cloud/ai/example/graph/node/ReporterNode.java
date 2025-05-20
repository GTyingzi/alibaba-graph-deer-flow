package com.alibaba.cloud.ai.example.graph.node;

import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.alibaba.cloud.ai.example.graph.util.TemplateUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yingzi
 * @date 2025/5/18 15:58
 */

public class ReporterNode implements NodeAction {

    private static final Logger logger = LoggerFactory.getLogger(ReporterNode.class);

    private final ChatClient chatClient;

    private static final String RESEARCH_FORMAT = "# Research Requirements\n\n## Task\n\n{0}\n\n## Description\n\n{1}";
    private final String REPORT_FORMAT = "IMPORTANT: Structure your report according to the format in the prompt. Remember to include:\n\n1. Key Points - A bulleted list of the most important findings\n2. Overview - A brief introduction to the topic\n3. Detailed Analysis - Organized into logical sections\n4. Survey Note (optional) - For more comprehensive reports\n5. Key Citations - List all references at the end\n\nFor citations, DO NOT include inline citations in the text. Instead, place all citations in the 'Key Citations' section at the end using the format: `- [Source Title](URL)`. Include an empty line between each citation for better readability.\n\nPRIORITIZE USING MARKDOWN TABLES for data presentation and comparison. Use tables whenever presenting comparative data, statistics, features, or options. Structure tables with clear headers and aligned columns. Example table format:\n\n| Feature | Description | Pros | Cons |\n|---------|-------------|------|------|\n| Feature 1 | Description 1 | Pros 1 | Cons 1 |\n| Feature 2 | Description 2 | Pros 2 | Cons 2 |";

    public ReporterNode(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        logger.info("Reporter node is running.");
        Plan currentPlan = state.value("current_plan", Plan.class)
                .orElseThrow(() -> new IllegalArgumentException("current_plan is missing"));

        List<Message> messages = new ArrayList<>(List.of(
                TemplateUtil.getMessage("reporter")
        ));
        messages.add(new UserMessage(MessageFormat.format(RESEARCH_FORMAT, currentPlan.getTitle(), currentPlan.getThought())));

        // 报告的格式
        messages.add(new SystemMessage(REPORT_FORMAT));

        List<String> observations = state.value("observations", List.class)
                .map(list -> (List<String>) list)
                .orElse(Collections.emptyList());
        for (String observation : observations) {
            messages.add(new UserMessage(observation));
        }
        logger.debug("Reporter node is running, messages: {}", messages);
        Flux<String> streamConent = chatClient.prompt().messages(messages).stream().content();
        String finalContent = streamConent.reduce((acc, next) -> acc + next).block();

        logger.info("final report: {}", finalContent);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("final_report", finalContent);
        return resultMap;
    }
}
