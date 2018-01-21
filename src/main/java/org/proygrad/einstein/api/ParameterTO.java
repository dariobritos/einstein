package org.proygrad.einstein.api;

public class ParameterTO {

    private Double value;
    private String type;
    private DistributionTO distributionTO;
    private String unit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DistributionTO getDistributionTO() {
        return distributionTO;
    }

    public void setDistributionTO(DistributionTO distributionTO) {
        this.distributionTO = distributionTO;
    }
}
