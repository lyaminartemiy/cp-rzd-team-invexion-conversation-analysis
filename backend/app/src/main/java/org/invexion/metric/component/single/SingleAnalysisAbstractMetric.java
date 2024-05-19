package org.invexion.metric.component.single;

import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.metric.component.AbstractMetric;

import java.math.BigDecimal;
import java.util.function.Function;

public abstract class SingleAnalysisAbstractMetric extends AbstractMetric {

    private final Function<FacadeNegotiationAnalysis, BigDecimal> calculationFunction;

    protected SingleAnalysisAbstractMetric(String title, String formula, Function<FacadeNegotiationAnalysis, BigDecimal> calculationFunction) {
        super(title, formula);
        this.calculationFunction = calculationFunction;
    }

    public BigDecimal calculate(FacadeNegotiationAnalysis facadeNegotiationAnalysis) {
        return calculationFunction.apply(facadeNegotiationAnalysis);
    }
}
