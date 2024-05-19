package org.invexion.metric.component.single.impl;

import org.invexion.metric.component.single.SingleAnalysisAbstractMetric;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class AllWordsCountMetric extends SingleAnalysisAbstractMetric {
    protected AllWordsCountMetric() {
        super("Количество слов всего для одного файла",
                null,
                analysis -> BigDecimal.valueOf(analysis.negotiationText().split(" ").length).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
