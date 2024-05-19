package org.invexion.metric.dto;

import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.negotiation.model.Negotiation;

public record NegotiationAnalysisDto(
        Negotiation negotiation,
        FacadeNegotiationAnalysis analysis
) {
}
