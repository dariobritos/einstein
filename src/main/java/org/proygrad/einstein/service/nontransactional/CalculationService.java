package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public ScenarioTO calculationResolve(ScenarioTO scenario) {

        ScenarioTO resolve = null;

        switch (scenario.getType()) {

            case SE_SURFACE_CRACK_STRAIGHT_PIPE:
                resolve = this.calculateSeSurfaceCrackStraightPipe.calculate(scenario);
                break;
            case SIMPLE_IRON_BAR:
                resolve = this.calculateSimpleIronBar.calculate(scenario);
                break;
        }

        return resolve;
    }

}
