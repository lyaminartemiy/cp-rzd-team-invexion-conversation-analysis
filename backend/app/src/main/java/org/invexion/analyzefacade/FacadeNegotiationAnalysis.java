package org.invexion.analyzefacade;

import java.util.List;

public record FacadeNegotiationAnalysis(
        String negotiationText,
        List<ViolatedPhraseBound> violatedPhrases
) {
    public record ViolatedPhraseBound(
            Long beginIndex,
            Long endIndex,
            String violatedRegulation,
            String violatedText
    ) {}
}
