package org.proygrad.einstein.service.transactional;


import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(CalculationServiceTX.class);


    @Autowired
    private PendingTaskDAO pendingTaskDAO;

    public String addCalculation(ScenarioTO scenarioTO) {
        LOGGER.info("Saving calculation...");
        PendingTaskEntity newTask = new PendingTaskEntity();
        newTask.setId(UUID.randomUUID().toString());
        newTask.setRequestId(scenarioTO.getId());
        newTask.setCreateDate(new Date());
        newTask.setRunning(false);
        newTask.setComplete(false);

        pendingTaskDAO.save(newTask);
        LOGGER.info("Calculation saved:" + newTask.getId());
        return newTask.getId();
    }
}
