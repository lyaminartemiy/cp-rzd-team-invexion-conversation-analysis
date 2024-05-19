package org.invexion.negotiation.service;

import org.invexion.negotiation.model.ViolationInterval;
import org.invexion.negotiation.model.repository.ViolationIntervalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViolationIntervalService {

    private final ViolationIntervalRepository violationIntervalRepository;

    public ViolationIntervalService(ViolationIntervalRepository violationIntervalRepository) {
        this.violationIntervalRepository = violationIntervalRepository;
    }

    public List<ViolationInterval> getViolationsByAnalysisId(Long analysisId) {
        return violationIntervalRepository.findAllByAnalysisId(analysisId);
    }
}
