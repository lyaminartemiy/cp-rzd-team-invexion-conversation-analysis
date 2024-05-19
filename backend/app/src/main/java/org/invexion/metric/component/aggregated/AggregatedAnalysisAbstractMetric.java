package org.invexion.metric.component.aggregated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.invexion.analyzefacade.FacadeNegotiationAnalysis;
import org.invexion.metric.component.AbstractMetric;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Getter
public abstract class AggregatedAnalysisAbstractMetric extends AbstractMetric {

    @JsonIgnore
    private final Function<List<FacadeNegotiationAnalysis>, BigDecimal> calculationFunction;

    protected AggregatedAnalysisAbstractMetric(String title, String formula, Function<List<FacadeNegotiationAnalysis>, BigDecimal> calculationFunction) {
        super(title, formula);
        this.calculationFunction = calculationFunction;
    }

    public BigDecimal calculate(List<FacadeNegotiationAnalysis> analysisList) {
        return calculationFunction.apply(analysisList);
    }
}
