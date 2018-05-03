package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.service.transactional.CalculationServiceTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    protected static final String SE_SURFACE_CRACK_STRAIGHT_PIPE = "SE_SURFACE_CRACK_STRAIGHT_PIPE";
    protected static final String CIRCULAR_SECTION_BAR_SUBJECTED_TO_TRACTION = "CIRCULAR_SECTION_BAR_SUBJECTED_TO_TRACTION";

    @Autowired
    private CalculationServiceTX calculationServiceTX;

    @Autowired
    private CalculateCircularSectionBarSubjectToTraction calculateCircularSectionBarSubjectToTraction;

    @Autowired
    private CalculateSeSurfaceCrackStraightPipe calculateSeSurfaceCrackStraightPipe;


    public ScenarioTO calculationResolve(ScenarioTO scenario) {

        ScenarioTO resolve = null;

        switch (scenario.getType()) {

            case SE_SURFACE_CRACK_STRAIGHT_PIPE:
                resolve = this.calculateSeSurfaceCrackStraightPipe.calculate(scenario);
                break;
            case CIRCULAR_SECTION_BAR_SUBJECTED_TO_TRACTION:
                resolve = this.calculateCircularSectionBarSubjectToTraction.calculate(scenario);
                break;
        }

        return resolve;
    }

}
