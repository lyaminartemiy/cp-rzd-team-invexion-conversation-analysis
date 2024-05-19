package org.invexion.metric.component.single.impl;

import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class ViolatedWordsCountMetric extends SingleAnalysisAbstractMetric {
    protected ViolatedWordsCountMetric() {
        super("Количество грязных слов для одного файла",
                null,
                analysis -> {
                    List<String> violatedPhrases = new ArrayList<>();
                    analysis.violatedPhrases().parallelStream().forEach(violatedPhraseBound -> {
                        String violatedPhrase = analysis.negotiationText().substring(Math.toIntExact(violatedPhraseBound.beginIndex()), Math.toIntExact(violatedPhraseBound.endIndex()));
                        violatedPhrases.add(violatedPhrase);
                    });
                    return BigDecimal.valueOf(violatedPhrases.parallelStream()
                            .map(phrase -> phrase.split(" "))
                            .map(words -> words.length)
                            .reduce(0, Integer::sum)).setScale(2, RoundingMode.HALF_UP);
                }
        );
    }
}
