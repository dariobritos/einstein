package org.proygrad.einstein.service.transactional;


import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.persistence.dao.PendingTaskDAO;
import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CalculationServiceTX {

    @Autowired
    private PendingTaskDAO pendingTaskDAO;

    public UUID addCalculation(ScenarioTO scenarioTO) {

        PendingTaskEntity newTask = new PendingTaskEntity();
        newTask.setRequestId(scenarioTO.getId());
        newTask.setCreateDate(new Date());
        newTask.setRunning(Boolean.FALSE);

        pendingTaskDAO.save(newTask);

        return newTask.getId();
    }

    public List<PendingTaskEntity> getPendingTask() {
        return pendingTaskDAO.readAll();
    }
}
