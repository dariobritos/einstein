package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationService {

    protected static final String SE_SURFACE_CRACK_STRAIGHT_PIPE = "SE_SURFACE_CRACK_STRAIGHT_PIPE";
    protected static final String SIMPLE_IRON_BAR = "SIMPLE_IRON_BAR";

    @Autowired
    private CalculationServiceTX calculationServiceTX;



    @Autowired
    private CalculateSimpleIronBar calculateSimpleIronBar;



    @Autowired
    private CalculateSeSurfaceCrackStraightPipe calculateSeSurfaceCrackStraightPipe;

    public List<CalculationTO> getCalculations() {
        return calculationServiceTX.getCalculations();
    }

    public void addCalculation(CalculationTO calculationTO) {
        calculationServiceTX.addCalculation(calculationTO);
    }

    public CalculationTO calculationResolve(CalculationTO calculationTO) {

        //TODO: Cargar en hazelcast
        CalculationTO output =null;
        switch (calculationTO.getType()) {

            case SE_SURFACE_CRACK_STRAIGHT_PIPE:
                output = this.calculateSeSurfaceCrackStraightPipe.calculate(calculationTO);
                break;
            case SIMPLE_IRON_BAR:
                output = this.calculateSimpleIronBar.calculate(calculationTO);
                break;
        }

        return output;
    }
}
