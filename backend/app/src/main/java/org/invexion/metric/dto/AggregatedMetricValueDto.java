package org.invexion.metric.dto;

import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;

import java.math.BigDecimal;

public record AggregatedMetricValueDto(
        AggregatedAnalysisAbstractMetric metric,
        BigDecimal value
) {
}
