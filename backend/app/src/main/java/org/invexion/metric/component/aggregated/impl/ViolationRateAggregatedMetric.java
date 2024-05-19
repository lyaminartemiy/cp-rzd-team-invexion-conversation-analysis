package org.invexion.metric.component.aggregated.impl;

import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.metric.component.aggregated.AggregatedAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class ViolationRateAggregatedMetric extends AggregatedAnalysisAbstractMetric {
    protected ViolationRateAggregatedMetric() {
        super("Процент грязности переговоров для всей выгрузки",
                "Количество грязных слов во всех переговорах / Количество слов во всех переговорах",
                analysisList -> {
                    List<String> violatedPhrases = new ArrayList<>();
                    analysisList.parallelStream().forEach(analysis -> {
                        analysis.violatedPhrases().parallelStream().forEach(violatedPhraseBound -> {
                            String violatedPhrase = analysis.negotiationText().substring(Math.toIntExact(violatedPhraseBound.beginIndex()), Math.toIntExact(violatedPhraseBound.endIndex()));
                            violatedPhrases.add(violatedPhrase);
                        }
                        );
                    });
                    BigDecimal violatedWordsCount = BigDecimal.valueOf(violatedPhrases.parallelStream()
                            .map(phrase -> phrase.split(" "))
                            .map(words -> words.length)
                            .reduce(0, Integer::sum)).setScale(2, RoundingMode.HALF_UP);
                    int allWordsCount = analysisList.parallelStream()
                            .map(FacadeNegotiationAnalysis::negotiationText)
                            .map(text -> text.split(" "))
                            .map(wordArray -> wordArray.length)
                            .reduce(Integer::sum).orElse(-1);
                    try {
                        return violatedWordsCount.divide(BigDecimal.valueOf(allWordsCount).setScale(2, RoundingMode.HALF_UP).equals(BigDecimal.ZERO) ? BigDecimal.valueOf(-1) : BigDecimal.valueOf(allWordsCount), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                    } catch (Exception e) {
                        return BigDecimal.valueOf(-1);
                    }
                }
        );
    }
}
