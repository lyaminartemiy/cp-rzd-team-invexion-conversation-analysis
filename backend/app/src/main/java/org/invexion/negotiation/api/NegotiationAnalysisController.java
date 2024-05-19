package org.invexion.negotiation.api;

import lombok.RequiredArgsConstructor;
import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.ViolationInterval;
import org.invexion.negotiation.model.dto.ViolationIntervalDto;
import org.invexion.negotiation.model.repository.NegotiationAnalysisRepository;
import org.invexion.negotiation.service.NegotiationService;
import org.invexion.negotiation.service.ViolationIntervalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/negotiation-analysis")
@RequiredArgsConstructor
@CrossOrigin
public class NegotiationAnalysisController {

    private final NegotiationAnalysisRepository negotiationAnalysisRepository;
    private final NegotiationService negotiationService;
    private final ViolationIntervalService violationIntervalService;

    @GetMapping("/detailsByIds")
    public ResponseEntity<List<NegotiationAnalysis>> calculateForNegotiations(@RequestParam("negotiationIds") String negotiationIds) {
        List<Negotiation> negotiations = negotiationService.findAllById(Arrays.stream(negotiationIds.split(","))
                .map(Long::valueOf)
                .toList()
        );
        List<NegotiationAnalysis> negotiationAnalysisList = negotiationAnalysisRepository.findAllByNegotiations(negotiations);
        return ResponseEntity.ok(negotiationAnalysisList);
    }

    @GetMapping("/getViolationsByAnalysisId")
    private ResponseEntity<List<ViolationInterval>> getViolationsByAnalysisId(@RequestParam("analysisId") Long analysisId){
        return ResponseEntity.ok(violationIntervalService.getViolationsByAnalysisId(analysisId));
    }
}
