package org.invexion.metric.component.single.impl;

import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class ViolationRateMetric extends SingleAnalysisAbstractMetric {
    public ViolationRateMetric() {
        super(
                "Процент грязности переговоров для одного файла",
                "Количество грязных слов в переговоре / Количество слов всего в переговоре",
                analysis -> {
                    List<String> violatedPhrases = new ArrayList<>();
                    analysis.violatedPhrases().parallelStream().forEach(violatedPhraseBound -> {
                        String violatedPhrase = analysis.negotiationText().substring(Math.toIntExact(violatedPhraseBound.beginIndex()), Math.toIntExact(violatedPhraseBound.endIndex()));
                        violatedPhrases.add(violatedPhrase);
                    });
                    int violatedWordsCount = violatedPhrases.parallelStream()
                            .map(phrase -> phrase.split(" "))
                            .map(words -> words.length)
                            .reduce(0, Integer::sum);
                    int allWordsCount = analysis.negotiationText().split(" ").length;
                    try {
                        return BigDecimal.valueOf(violatedWordsCount).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(allWordsCount).equals(BigDecimal.ZERO) ? BigDecimal.valueOf(-1) : BigDecimal.valueOf(allWordsCount), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                    } catch (Exception e) {
                        return BigDecimal.valueOf(-1);
                    }
                }
        );
    }
}