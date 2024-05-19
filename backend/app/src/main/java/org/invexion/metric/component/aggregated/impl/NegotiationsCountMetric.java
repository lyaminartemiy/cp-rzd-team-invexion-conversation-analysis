package org.invexion.metric.component.aggregated.impl;

import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class NegotiationsCountMetric extends AggregatedAnalysisAbstractMetric {
    protected NegotiationsCountMetric() {
        super("Количество переговоров всего",
                null,
                analysisList -> BigDecimal.valueOf(analysisList.size()).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
