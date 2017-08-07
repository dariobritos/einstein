package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationService {

    @Autowired
    private CalculationServiceTX calculationServiceTX;


    public void addCalculation(CalculationTO calculationTO) {
        calculationServiceTX.addCalculation(calculationTO);
    }

    public List<CalculationTO> getCalculations() {
        return calculationServiceTX.getCalculations();
    }
}
