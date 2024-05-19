package org.invexion.metric.component.aggregated.impl;

import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class AllWordsCountAggregatedMetric extends AggregatedAnalysisAbstractMetric {
    protected AllWordsCountAggregatedMetric() {
        super("Количество слов всего для всей выгрузки",
                null,
                analysisList -> BigDecimal.valueOf(analysisList.parallelStream()
                        .map(FacadeNegotiationAnalysis::negotiationText)
                        .map(text -> text.split(" "))
                        .map(wordArray -> wordArray.length)
                        .reduce(Integer::sum).orElse(-1)).setScale(2, RoundingMode.HALF_UP)
                );
    }
}
