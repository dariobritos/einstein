package org.proygrad.einstein.api;

import java.util.Map;

public class Distribution {


    public enum DistributionType {
        NORMAL, POISSON, LOGNORMAL
    }

    private DistributionType type;
    private Map<String, Double> parameters;

    public DistributionType getType() {
        return type;
    }

    public void setType(DistributionType type) {
        this.type = type;
    }

    public Map<String, Double> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Double> parameters) {
        this.parameters = parameters;
    }
}
