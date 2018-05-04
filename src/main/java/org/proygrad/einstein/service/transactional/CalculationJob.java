package org.proygrad.einstein.service.transactional;

import org.apache.log4j.Logger;
import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.proygrad.einstein.rest.client.TuringClient;
import org.proygrad.einstein.service.nontransactional.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class CalculationJob {

    private static final Logger LOGGER = Logger.getLogger(CalculationJob.class);


    @Autowired
    private PendingTaskServiceTX pendingTaskServiceTX;

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private TuringClient turingClient;

    @Scheduled(fixedDelay = 7200, initialDelay = 900)
    public void run() {

        LOGGER.info("Getting pending calculations...");
        List<PendingTaskEntity> pendingTaskEntities = pendingTaskServiceTX.getPendingTask();

        for (PendingTaskEntity pendingTask : pendingTaskEntities) {

            pendingTaskServiceTX.setCompleteTask(pendingTask.getId(), true, false);

            ScenarioTO scenario = turingClient.getScenario(pendingTask.getRequestId());

            try {
                LOGGER.info("Starting scenario calculation: " + scenario.getId());
                ScenarioTO result = calculationService.calculationResolve(scenario);
                LOGGER.info("Scenario calculation finished: " + scenario.getId());
                turingClient.sendResult(result);
                // marcar como realizada
                pendingTaskServiceTX.setCompleteTask(pendingTask.getId(), false, true);
            } catch (Exception ex) {
                // marcar como not running
                pendingTaskServiceTX.setCompleteTask(pendingTask.getId(), false, false);

                throw ex;

            }

        }
    }

}
