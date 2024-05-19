package org.invexion.metric.component.aggregated.impl;

import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class NonViolatedNegotiationsPercentageMetric extends AggregatedAnalysisAbstractMetric {
    protected NonViolatedNegotiationsPercentageMetric() {
        super("Процент чистоты выгрузки",
                "Количество чистых переговоров / Количество переговоров всего",
                analysisList -> {
                    BigDecimal nonViolatedNegotiationsCount = BigDecimal.valueOf(analysisList.parallelStream()
                            .filter(analysis -> analysis.violatedPhrases().isEmpty())
                            .count()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal allNegotiationsCount = BigDecimal.valueOf(analysisList.size()).setScale(2, RoundingMode.HALF_UP);
                    System.out.println();
                    try {
                        return nonViolatedNegotiationsCount.divide(allNegotiationsCount.equals(BigDecimal.ZERO) ? BigDecimal.valueOf(-1) : allNegotiationsCount, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                    } catch (Exception e) {
                        return BigDecimal.valueOf(-1);
                    }
                }
        );
    }
}
