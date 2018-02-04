package org.proygrad.einstein.api;


import java.util.List;

public class ScenarioTO {

    private String id;
    private String type;
    private String unitSystem;
    private List<ParameterTO> parameters;
    private List<CommonItemTO> configuration;
    private String comments;
    private List<CommonItemTO> output;

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

    public String getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(String unitSystem) {
        this.unitSystem = unitSystem;
    }

    public List<ParameterTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterTO> parameters) {
        this.parameters = parameters;
    }

    public List<CommonItemTO> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<CommonItemTO> configuration) {
        this.configuration = configuration;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<CommonItemTO> getOutput() {
        return output;
    }

    public void setOutput(List<CommonItemTO> output) {
        this.output = output;
    }
}

