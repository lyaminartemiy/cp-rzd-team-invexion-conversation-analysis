package org.invexion.metric.component.aggregated.impl;

import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ViolatedNegotiationsPercentageMetric extends AggregatedAnalysisAbstractMetric {
    protected ViolatedNegotiationsPercentageMetric() {
        super("Процент грязности выгрузки",
                "Количество грязных переговоров / Количество переговоров всего",
                analysisList -> {
                    BigDecimal violatedNegotiationsCount = BigDecimal.valueOf(analysisList.parallelStream()
                            .filter(analysis -> !analysis.violatedPhrases().isEmpty())
                            .count());
                    BigDecimal allNegotiationsCount = BigDecimal.valueOf(analysisList.size());
                    try {
                        return violatedNegotiationsCount.divide(allNegotiationsCount.equals(BigDecimal.ZERO) ? BigDecimal.valueOf(-1) : allNegotiationsCount, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                    } catch (Exception e) {
                        return BigDecimal.valueOf(-1);
                    }
                }
        );
    }
}
