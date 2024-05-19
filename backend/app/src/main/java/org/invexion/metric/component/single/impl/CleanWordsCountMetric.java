package org.invexion.metric.component.single.impl;

import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class CleanWordsCountMetric extends SingleAnalysisAbstractMetric {
    protected CleanWordsCountMetric() {
        super("Количество чистых слов для одного файла",
                null,
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
                    return BigDecimal.valueOf(allWordsCount - violatedWordsCount).setScale(2, RoundingMode.HALF_UP);
                }
        );
    }
}
