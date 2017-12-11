package org.proygrad.einstein.api;

public class Parameter {

    public enum ValueType {
        STATIC, VARIABLE
    }

    public enum UnitType {
        MILLIMETRE, CENTIMETRE, MEGAPASCAL, KILOPASCAL,
        THOU, INCH, FOOT
    }

    private Double value;
    private ValueType type;
    private Distribution distribution;
    private UnitType unit;

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }
}
