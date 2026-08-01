/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.auditagent.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** 根据异常率、重要性和控制缺陷生成风险导向审计样本。 */
@Service
public class AuditSamplingService {
    public SamplingResult recommend(SamplingRequest request) {
        int riskScore = Math.min(100,
            (int) Math.round(request.anomalyRate() * 100 * 0.45)
                + (int) Math.round(request.materialityScore() * 0.30)
                + (request.controlWeakness() ? 20 : 0)
                + Math.min(20, request.previousFindings() * 4));
        double ratio = riskScore >= 75 ? 0.30 : riskScore >= 50 ? 0.20 : riskScore >= 25 ? 0.12 : 0.08;
        int sampleSize = Math.min(request.populationSize(), Math.max(25, (int) Math.ceil(request.populationSize() * ratio)));
        List<String> strata = new ArrayList<>(List.of("高金额项目", "异常评分前列项目", "随机基准样本"));
        if (request.controlWeakness()) strata.add("控制例外关联项目");
        String assurance = riskScore >= 75 ? "HIGH" : riskScore >= 40 ? "MODERATE" : "STANDARD";
        return new SamplingResult(riskScore, sampleSize, assurance, List.copyOf(strata), request.controlWeakness() ? "优先执行控制测试并扩大实质性程序" : "按风险分层抽样并保留选择依据");
    }

    public record SamplingRequest(
        @NotBlank(message = "请输入审计总体名称") String populationName,
        @Positive int populationSize,
        @DecimalMin("0.0") @DecimalMax("1.0") double anomalyRate,
        @DecimalMin("0.0") @DecimalMax("100.0") double materialityScore,
        boolean controlWeakness,
        @PositiveOrZero int previousFindings
    ) {}

    public record SamplingResult(int riskScore, int recommendedSampleSize, String assuranceLevel, List<String> strata, String nextAction) {}
}
