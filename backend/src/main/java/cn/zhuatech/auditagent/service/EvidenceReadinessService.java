/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.auditagent.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** 在审计底稿提交复核前检查必需证据、来源追溯和复核人分配情况。 */
@Service
public class EvidenceReadinessService {
    public ReadinessResult check(ReadinessRequest request) {
        Map<String, String> attached = new LinkedHashMap<>();
        request.attachedEvidence().stream().map(String::trim).filter(item -> !item.isEmpty())
            .forEach(item -> attached.putIfAbsent(item.toLowerCase(Locale.ROOT), item));
        List<String> missing = request.requiredEvidence().stream().map(String::trim).filter(item -> !item.isEmpty())
            .filter(item -> !attached.containsKey(item.toLowerCase(Locale.ROOT))).distinct().toList();
        long uniqueRequired = request.requiredEvidence().stream().map(String::trim).filter(item -> !item.isEmpty())
            .map(item -> item.toLowerCase(Locale.ROOT)).distinct().count();
        int completeness = uniqueRequired == 0 ? 100 : (int) Math.round((uniqueRequired - missing.size()) * 100.0 / uniqueRequired);

        String state = !missing.isEmpty() ? "INCOMPLETE"
            : !request.sourceTraceable() || !request.reviewerAssigned() ? "REVIEW" : "READY";
        List<String> actions = java.util.stream.Stream.of(
            missing.isEmpty() ? null : "补充缺失证据: " + String.join("、", missing),
            request.sourceTraceable() ? null : "补录证据来源、提取时间和责任人",
            request.reviewerAssigned() ? null : "分配独立复核人"
        ).filter(java.util.Objects::nonNull).toList();
        return new ReadinessResult(state, completeness, missing, actions,
            "READY".equals(state) ? "证据包可提交复核" : "完成整改动作后重新检查");
    }

    public record ReadinessRequest(
        @NotBlank(message = "请输入底稿编号") String workpaperCode,
        @NotEmpty(message = "请配置必需证据") List<String> requiredEvidence,
        List<String> attachedEvidence,
        boolean sourceTraceable,
        boolean reviewerAssigned
    ) {
        public ReadinessRequest {
            attachedEvidence = attachedEvidence == null ? List.of() : List.copyOf(attachedEvidence);
        }
    }

    public record ReadinessResult(String state, int completenessPercent, List<String> missingEvidence, List<String> remediationActions, String guidance) {}
}
