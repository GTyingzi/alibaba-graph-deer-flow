import com.alibaba.cloud.ai.example.graph.model.Plan;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yingzi
 * @date 2025/5/19 18:18
 */

public class PlanDeserializerTest {

    public static void main(String[] args) throws Exception {
        String json = """
            {
              "has_enough_context": false,
              "thought": "用户询问了如何制作草莓蛋糕。尽管背景调查提供了关于草莓蛋糕的基本步骤，但这些信息还不足以编写一个全面详细的报告。",
              "title": "草莓蛋糕的详细制作方法",
              "steps": [
                {
                  "need_web_search": true,
                  "title": "草莓蛋糕所需材料清单",
                  "description": "收集制作草莓蛋糕所需的全部原材料...",
                  "step_type": "research"
                },
                {
                  "need_web_search": true,
                  "title": "草莓酱的具体制作方法",
                  "description": "找到草莓酱的详尽制作过程...",
                  "step_type": "research"
                }
              ]
            }
        """;

        ObjectMapper mapper = new ObjectMapper();
        Plan plan = mapper.readValue(json, Plan.class);
        System.out.println(plan);
    }
}
