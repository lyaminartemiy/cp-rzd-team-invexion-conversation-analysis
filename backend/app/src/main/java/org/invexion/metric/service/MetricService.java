package org.invexion.metric.service;

import lombok.RequiredArgsConstructor;
import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.metric.dto.AggregatedMetricValueDto;
import org.invexion.metric.dto.CalculateMetricsDto;
import org.invexion.metric.dto.NegotiationAnalysisDto;
import org.invexion.metric.dto.SingleMetricsValueDto;
import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.repository.NegotiationAnalysisRepository;
import org.invexion.negotiation.model.repository.ViolationIntervalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final List<SingleAnalysisAbstractMetric> singleAnalysisMetrics;
    private final List<AggregatedAnalysisAbstractMetric> aggregatedAnalysisMetrics;
    private final ViolationIntervalRepository violationIntervalRepository;
    private final NegotiationAnalysisRepository negotiationAnalysisRepository;

    public CalculateMetricsDto calculateMetrics(List<NegotiationAnalysis> negotiationAnalysisList) {
        List<NegotiationAnalysisDto> negotiationAnalysisDtos = negotiationAnalysisList.parallelStream()
                .map(analysis -> new NegotiationAnalysisDto(
                        analysis.getNegotiation(),
                        new FacadeNegotiationAnalysis(
                                analysis.getNegotiationText(),
                                violationIntervalRepository
                                        .findByNegotiationAnalysis(analysis).parallelStream()
                                        .map(interval -> new FacadeNegotiationAnalysis.ViolatedPhraseBound(
                                                        interval.getBeginIndex(),
                                                        interval.getEndIndex(),
                                                        "violatedRegulation",
                                                        "violatedText"
                                                )
                                        )
                                        .toList()
                        )
                )).toList();

        List<FacadeNegotiationAnalysis> analysisList = negotiationAnalysisDtos.parallelStream().map(NegotiationAnalysisDto::analysis).toList();
        List<AggregatedMetricValueDto> aggregatedMetrics = aggregatedAnalysisMetrics.parallelStream()
                .map(metric -> new AggregatedMetricValueDto(metric, metric.calculate(analysisList)))
                .toList();
        List<SingleMetricsValueDto> singleMetrics = negotiationAnalysisDtos.parallelStream()
                .map(negotiationAnalysisDto -> {
                    List<SingleMetricsValueDto.SingleMetricValueDto> singleMetricValueDtos = new ArrayList<>();
                    singleAnalysisMetrics.parallelStream().forEach(metric -> {System.out.println(metric.getTitle()); singleMetricValueDtos.add(new SingleMetricsValueDto.SingleMetricValueDto(metric, metric.calculate(negotiationAnalysisDto.analysis())));});
                    return new SingleMetricsValueDto(negotiationAnalysisRepository.findByNegotiation(negotiationAnalysisDto.negotiation()), singleMetricValueDtos);
                })
                .toList();
        return new CalculateMetricsDto(
                aggregatedMetrics,
                singleMetrics
        );
    }
}
