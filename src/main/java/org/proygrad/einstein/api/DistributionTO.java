package org.proygrad.einstein.api;

import java.util.List;

public class DistributionTO {

    private String type;
    private List<CommonItemTO> parameters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CommonItemTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<CommonItemTO> parameters) {
        this.parameters = parameters;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
