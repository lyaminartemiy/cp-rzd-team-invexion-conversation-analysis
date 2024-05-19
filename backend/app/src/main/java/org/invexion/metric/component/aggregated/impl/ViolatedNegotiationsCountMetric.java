package org.invexion.metric.component.aggregated.impl;

import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ViolatedNegotiationsCountMetric extends AggregatedAnalysisAbstractMetric {
    protected ViolatedNegotiationsCountMetric() {
        super("Количество грязных переговоров",
                null,
                analysisList -> BigDecimal.valueOf(analysisList.parallelStream()
                        .filter(analysis -> !analysis.violatedPhrases().isEmpty())
                        .count()).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
