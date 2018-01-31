package org.proygrad.einstein.service.transactional;


import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.persistence.dao.PendingTaskDAO;
import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class CalculationServiceTX {

    @Autowired
    private PendingTaskDAO pendingTaskDAO;

    public String addCalculation(ScenarioTO scenarioTO) {

        PendingTaskEntity newTask = new PendingTaskEntity();
        newTask.setId(UUID.randomUUID().toString());
        newTask.setRequestId(scenarioTO.getId());
        newTask.setCreateDate(new Date());
        newTask.setRunning(false);

        pendingTaskDAO.save(newTask);

        return newTask.getId();
    }
}
