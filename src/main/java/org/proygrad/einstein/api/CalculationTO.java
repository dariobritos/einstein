package org.proygrad.einstein.api;


import java.util.Map;

public class CalculationTO {
    public enum UnitSystem {
        INTERNATIONAL_SYSTEM, US_SYSTEM
    }

    private String id;
    private String type;
    private UnitSystem unit;

    private Map<String, Parameter> parameters;
    private Map<String, Parameter> materials;

    private Map<String, Double> configurations;

    private Map<String, Object> output;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UnitSystem getUnit() {
        return unit;
    }

    public void setUnit(UnitSystem unit) {
        this.unit = unit;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Parameter> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, Parameter> materials) {
        this.materials = materials;
    }

    public Map<String, Double> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Double> configurations) {
        this.configurations = configurations;
    }

    public Map<String, Object> getOutput() {
        return output;
    }

    public void setOutput(Map<String, Object> output) {
        this.output = output;
    }

}
