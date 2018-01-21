package org.proygrad.einstein.service.transactional;


import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.persistence.dao.ScenarioDAO;
import org.proygrad.einstein.persistence.entities.ScenarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculationServiceTX {

    @Autowired
    private ScenarioDAO scenarioDAO;

    @Autowired
    private ScenarioMapper scenarioMapper;

    public void addCalculation(ScenarioTO scenarioTO) {
        ScenarioEntity entity = scenarioMapper.toEntity(scenarioTO);
        scenarioDAO.save(entity);
    }

    public List<ScenarioTO> getCalculations() {

        return scenarioDAO.readAll().stream().map(x->{
            return scenarioMapper.toTransferObject(x);
        }).collect(Collectors.toList());
    }
}
