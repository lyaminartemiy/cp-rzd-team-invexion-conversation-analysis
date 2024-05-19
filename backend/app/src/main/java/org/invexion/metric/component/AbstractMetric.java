package org.invexion.metric.component;

import lombok.Getter;

@Getter
public abstract class AbstractMetric {

    private final String title;
    private final String formula;

    protected AbstractMetric(String title, String formula) {
        this.title = title;
        this.formula = formula;
    }
}
