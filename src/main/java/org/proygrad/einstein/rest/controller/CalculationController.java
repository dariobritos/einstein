package org.proygrad.einstein.rest.controller;

import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.service.nontransactional.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalculationController {


    @Autowired
    CalculationService calculationService;

    @RequestMapping(value = "/calculation", method = RequestMethod.GET)
    public List<CalculationTO> calculation() {


        return calculationService.getCalculations();
    }

    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    public void calculationAdd(@RequestBody CalculationTO calculationTO) {

        calculationService.addCalculation(calculationTO);
    }

}
