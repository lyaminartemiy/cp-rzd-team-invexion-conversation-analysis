package org.invexion.metric.dto;

import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.invexion.negotiation.model.NegotiationAnalysis;

import java.math.BigDecimal;
import java.util.List;

public record SingleMetricsValueDto(
        NegotiationAnalysis negotiationAnalysis,
        List<SingleMetricValueDto> singleMetricValues
        ) {

        public record SingleMetricValueDto(
                SingleAnalysisAbstractMetric metric,
                BigDecimal value
        ) {}
}
