package org.invexion.metric.api;

import org.invexion.metric.dto.CalculateMetricsDto;
import org.invexion.metric.service.MetricService;
import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.repository.NegotiationAnalysisRepository;
import org.invexion.negotiation.model.repository.NegotiationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin
public class MetricController {

    private final NegotiationRepository negotiationRepository;
    private final NegotiationAnalysisRepository negotiationAnalysisRepository;
    private final MetricService metricService;

    public MetricController(NegotiationRepository negotiationRepository,
                            NegotiationAnalysisRepository negotiationAnalysisRepository,
                            MetricService metricService
    ) {this.negotiationRepository = negotiationRepository;
        this.negotiationAnalysisRepository = negotiationAnalysisRepository;
        this.metricService = metricService;
    }

    @GetMapping("/calculate")
    public CalculateMetricsDto calculateForNegotiations(@RequestParam("negotiationIds") String negotiationIds) {
        List<Negotiation> negotiations = negotiationRepository.findAllById(Arrays.stream(negotiationIds.split(","))
                .map(Long::valueOf)
                .toList()
        );
        List<NegotiationAnalysis> negotiationAnalysisList = negotiationAnalysisRepository.findAllByNegotiations(negotiations);
        return metricService.calculateMetrics(negotiationAnalysisList);
    }

    @GetMapping("/calculate/all")
    public CalculateMetricsDto calculateForAllNegotiations() {
        List<NegotiationAnalysis> negotiationAnalysisList = negotiationAnalysisRepository.findAll();
        return metricService.calculateMetrics(negotiationAnalysisList);
    }
}
