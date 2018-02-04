package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.CommonItemTO;
import org.proygrad.einstein.api.ParameterTO;
import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.util.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculateSimpleIronBar {


    private static final String BAR_LOAD = "BAR_LOAD";
    private static final String BAR_STRENGTH = "BAR_STRENGTH";

    private static final String SEED = "SEED";
    private static final String PRECISION = "PRECISION";

    private static final String FAILURE_PROBABILITY = "FAILURE_PROBABILITY";

    private static final String VARIANCE = "VARIANCE";
    private static final String SCALE = "SCALE";


    private Map<String, AbstractRealDistribution> distributionMap = new HashMap<String, AbstractRealDistribution>();
    private String unitSystem;

    public ScenarioTO calculate(ScenarioTO scenario) {

        scenario = calculateSimple(scenario);

        return scenario;
    }

    public ScenarioTO calculateSimple(ScenarioTO scenario) {

        unitSystem = scenario.getUnitSystem();

        ParameterTO barLoadParameterTO = ParameterUtil.getParameter(scenario.getParameters(), BAR_LOAD);
        ParameterTO barStrengthParameterTO = ParameterUtil.getParameter(scenario.getParameters(), BAR_STRENGTH);

        Double seed = CommonItemUtil.getValue(scenario.getConfiguration(), SEED);

        RandomDataGenerator ran = new RandomDataGenerator();
        RandomGenerator randomGenerator = ran.getRandomGenerator();
        randomGenerator.setSeed(seed.longValue());


        this.loadDistributionMap(BAR_LOAD, barLoadParameterTO, randomGenerator);
        this.loadDistributionMap(BAR_STRENGTH, barStrengthParameterTO, randomGenerator);


        Double precision = CommonItemUtil.getValue(scenario.getConfiguration(), PRECISION);
        Integer failCount = 0;

        for (double i = 0; i < precision; i++) {

            Double barLoadSim = simulate(BAR_LOAD, barLoadParameterTO);
            Double barStrengthSim = simulate(BAR_STRENGTH, barStrengthParameterTO);

            if (barLoadSim > barStrengthSim) {
                failCount++;
            }
        }


        Double prob = failCount / precision;


        if (scenario.getOutput() == null) {
            List<CommonItemTO> output = new ArrayList<CommonItemTO>();

            scenario.setOutput(output);
        }

        CommonItemTO out = new CommonItemTO();
        out.setCode(FAILURE_PROBABILITY);
        out.setValue(prob.toString());

        scenario.getOutput().add(out);


        return scenario;

    }

    private Double simulate(String key, ParameterTO variable) {
        if (ValueType.VARIABLE.equals(variable.getType())) {

            ParameterTO p = new ParameterTO();
            p.setUnit(variable.getUnit());
            p.setType(variable.getType());
            p.setValue(distributionMap.get(key).sample());

            return loadAndNormalize(p).getValue();
        }

        return loadAndNormalize(variable).getValue();
    }

    private void loadDistributionMap(String key, ParameterTO variable, RandomGenerator randomGenerator) {

        switch (variable.getDistribution().getType()) {
            case DistributionType.NORMAL:
                Double variance = CommonItemUtil.getValue(variable.getDistribution().getParameters(), VARIANCE);
                Double mean = variable.getValue();
                distributionMap.put(key, new NormalDistribution(randomGenerator, mean, variance));
                break;
            case DistributionType.LOGNORMAL:
                Double scale = CommonItemUtil.getValue(variable.getDistribution().getParameters(), SCALE);
                Double shape = variable.getValue();
                distributionMap.put(key, new LogNormalDistribution(randomGenerator, scale, shape));
                break;
            case DistributionType.POISSON:
                //TODO: VER DE AGREGAR! PARA MAS FUNCIONABILIDAD.
                break;
        }
    }


    //TODO: completar segun se necesite!!!
    private ParameterTO loadAndNormalize(ParameterTO variable) {
        switch (unitSystem) {
            case UnitSystem.INTERNATIONAL:
                switch (variable.getUnit()) {
                    case UnitType.CENTIMETRE:
                        variable.setValue(variable.getValue() / 10);
                        variable.setUnit(UnitType.MILLIMETRE);
                        break;
                    case UnitType.KILOPASCAL:
                        variable.setValue(variable.getValue() * 1000);
                        variable.setUnit(UnitType.MEGAPASCAL);
                        break;
                }
                break;
            case UnitSystem.UNITEDSTATES:
                switch (variable.getUnit()) {
                    case UnitType.THOU:
                        variable.setValue(variable.getValue() * 0.0254);
                        variable.setUnit(UnitType.MILLIMETRE);
                        break;
                    case UnitType.INCH:
                        variable.setValue(variable.getValue() * 25.4);
                        variable.setUnit(UnitType.MILLIMETRE);
                        break;
                    case UnitType.FOOT:
                        variable.setValue(variable.getValue() * 304.8);
                        variable.setUnit(UnitType.MILLIMETRE);
                        break;
                }
                break;
        }
        return variable;
    }
}
