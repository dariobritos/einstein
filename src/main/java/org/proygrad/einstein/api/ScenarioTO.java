package org.proygrad.einstein.api;


import java.util.Map;

public class ScenarioTO {

    private String requestCalculation;

    private String id;
    private String type;
    private String unit;

    private Map<String, ParameterTO> parameters;
    private Map<String, ParameterTO> materials;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, ParameterTO> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, ParameterTO> parameters) {
        this.parameters = parameters;
    }

    public Map<String, ParameterTO> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, ParameterTO> materials) {
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

    public String getRequestCalculation() {
        return requestCalculation;
    }

    public void setRequestCalculation(String requestCalculation) {
        this.requestCalculation = requestCalculation;
    }

}
