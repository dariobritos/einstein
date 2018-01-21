package org.proygrad.einstein.service.transactional;

import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.persistence.entities.ScenarioEntity;
import org.springframework.stereotype.Service;

@Service
public class ScenarioMapper {


    public ScenarioEntity toEntity(ScenarioTO data){
        ScenarioEntity entity = new ScenarioEntity();

        //entity.setId(data.getId());
        entity.setType(data.getType());
        entity.setUnit(data.getUnit());

        entity.setParameters(data.getParameters());
    //    entity.setRequestCalculation(data.getRequestCalculation());

        return entity;
    }


    public ScenarioTO toTransferObject(ScenarioEntity data){
        ScenarioTO entity = new ScenarioTO();

        entity.setId(data.getId());
        entity.setType(data.getType());
        entity.setUnit(data.getUnit());
   //     entity.setRequestCalculation(data.getRequestCalculation());

        return entity;
    }


}
