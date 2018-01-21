package org.proygrad.einstein.persistence.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCENARIO")
public class ScenarioEntity extends AbstractHibernateEntity<UUID>{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private UUID id;

    @Column(name="TYPE")
    private String type;

    @Column(name="UNIT")
    private String unit;

    @Column(name="PARAMETERS", columnDefinition = "json")
    private Object parameters;

  /*  private Map<String, ParameterTO> materials;

    private Map<String, Double> configurations;

    private Map<String, Object> output;

    private UUID requestCalculation;
*/

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public Object getParameters() {
        return parameters;
    }

  /*  public UUID getRequestCalculation() {
        return requestCalculation;
    }

    public void setRequestCalculation(UUID requestCalculation) {
        this.requestCalculation = requestCalculation;
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
    }*/
}
