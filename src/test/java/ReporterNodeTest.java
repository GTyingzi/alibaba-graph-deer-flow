import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/19 18:32
 */

public class ReporterNodeTest {

    private static final String RESEARCH_FORMAT = "# Research Requirements\n\n## Task\n\n{0}\n\n## Description\n\n{1}";

    public static void main(String[] args) {
        String title = "草莓蛋糕";
        String thought = "草莓蛋糕由草莓制成..."; // 简化描述

        String formatted = MessageFormat.format(RESEARCH_FORMAT, title, thought);
        System.out.println(formatted);
    }
}
