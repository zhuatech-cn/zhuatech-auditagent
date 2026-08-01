/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.auditagent.agent;
import org.springframework.stereotype.Component; import java.util.List; import java.util.Map;
/** 审计智能体运行边界；证据采集与底稿结论始终保留人工复核。 */
public interface AgentRuntime {AgentResult run(AgentRequest request);record AgentRequest(String objective,Map<String,String> context){}record AgentStep(String name,String status,String evidence){}record AgentResult(String runtime,String summary,List<AgentStep> steps,Map<String,Object> metrics){}}
@Component class DemoAgentRuntime implements AgentRuntime {public AgentResult run(AgentRequest request){return new AgentResult("local-audit-demo","已匹配控制点、抽取证据并形成底稿草案，审计结论等待签署。",List.of(new AgentStep("控制匹配","COMPLETED","匹配 9 个关键控制"),new AgentStep("证据测试","COMPLETED","完成 24 个样本核验"),new AgentStep("结论签署","PENDING","等待项目经理复核")),Map.of("controls",9,"samples",24,"objectiveLength",request.objective().length()));}}
