package org.proygrad.einstein.service.transactional;

import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.proygrad.einstein.service.nontransactional.CalculationService;
import org.proygrad.einstein.service.nontransactional.TuringClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class CalculationJob {

    @Autowired
    private PendingTaskServiceTX pendingTaskServiceTX;

    @Autowired
    private CalculationService calculationService;

     //@Autowired
    private TuringClient turingClient;

    @Scheduled(fixedDelay = 7200, initialDelay = 900)
    public void run(){

        List<PendingTaskEntity> pendingTaskEntities = pendingTaskServiceTX.getPendingTask();

        for(PendingTaskEntity pendingTask : pendingTaskEntities) {

            pendingTaskServiceTX.setCompleteTask(pendingTask.getId(), true);

            //ScenarioTO scenario = turingClient.getScenario(pendingTask.getRequestId());

           // ScenarioTO result = calculationService.calculationResolve(scenario);

            // marcar como realizada
           // pendingTaskServiceTX.setCompleteTask(pendingTask.getId(), false);

            // avisar o enviar el resultado a turing.
           // turingClient.sendResult(result);

        }
    }

}
