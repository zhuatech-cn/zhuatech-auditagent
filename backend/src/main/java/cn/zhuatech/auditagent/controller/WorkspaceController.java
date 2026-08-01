/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.auditagent.controller;

import cn.zhuatech.auditagent.agent.AgentRuntime;
import cn.zhuatech.auditagent.common.ApiResponse;
import cn.zhuatech.auditagent.dto.AuditAgentDto.*;
import cn.zhuatech.auditagent.service.AuditAgentService;
import cn.zhuatech.auditagent.service.AuditSamplingService;
import cn.zhuatech.auditagent.service.EvidenceReadinessService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('DOMAIN_USER','ADMIN')")
public class WorkspaceController {
    private final AuditAgentService service;
    private final AgentRuntime runtime;
    private final AuditSamplingService sampling;
    private final EvidenceReadinessService evidenceReadiness;

    public WorkspaceController(AuditAgentService service, AgentRuntime runtime, AuditSamplingService sampling, EvidenceReadinessService evidenceReadiness) {
        this.service = service;
        this.runtime = runtime;
        this.sampling = sampling;
        this.evidenceReadiness = evidenceReadiness;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Dashboard> dashboard() { return ApiResponse.ok(service.shopfloorDashboard()); }

    @PostMapping("/work-orders/{id}/reports")
    public ApiResponse<ReportResult> report(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.ok("反馈提交成功", service.report(id, request));
    }

    @PostMapping("/agent-preview")
    public ApiResponse<AgentRuntime.AgentResult> preview(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(runtime.run(new AgentRuntime.AgentRequest(body.getOrDefault("objective", "执行采购审计"), Map.of("mode", "demo"))));
    }

    @PostMapping("/audit-sampling")
    public ApiResponse<AuditSamplingService.SamplingResult> recommend(@Valid @RequestBody AuditSamplingService.SamplingRequest request) {
        return ApiResponse.ok("审计抽样建议生成完成", sampling.recommend(request));
    }

    @PostMapping("/evidence-readiness")
    public ApiResponse<EvidenceReadinessService.ReadinessResult> checkEvidence(@Valid @RequestBody EvidenceReadinessService.ReadinessRequest request) {
        return ApiResponse.ok("审计证据完整度检查完成", evidenceReadiness.check(request));
    }
}
