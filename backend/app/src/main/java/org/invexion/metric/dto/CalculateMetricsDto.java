package org.invexion.metric.dto;

import java.util.List;

public record CalculateMetricsDto(
        List<AggregatedMetricValueDto> aggregatedMetrics,
        List<SingleMetricsValueDto> singleMetrics
) {
}
