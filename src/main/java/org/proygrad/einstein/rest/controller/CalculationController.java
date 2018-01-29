package org.proygrad.einstein.rest.controller;

import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CalculationController {

    @Autowired
    private CalculationServiceTX calculationServiceTx;

    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    public UUID calculationRequest(@RequestBody ScenarioTO scenarioTO) {
        return calculationServiceTx.addCalculation(scenarioTO);
    }


}
