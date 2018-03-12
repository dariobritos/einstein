package org.proygrad.einstein.rest.controller;

import org.apache.log4j.Logger;
import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculationController {

    private static final Logger LOGGER = Logger.getLogger(CalculationServiceTX.class);

    @Autowired
    private CalculationServiceTX calculationServiceTx;

    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    public String calculationRequest(@RequestBody ScenarioTO scenarioTO) {
        LOGGER.info("POST Calculation request received...");
        return calculationServiceTx.addCalculation(scenarioTO);
    }


}
