package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.ParameterTO;
import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.util.DistributionType;
import org.proygrad.einstein.util.UnitSystem;
import org.proygrad.einstein.util.UnitType;
import org.proygrad.einstein.util.ValueType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

       /* unitSystem = scenario.getUnit();
        ParameterTO barLoadParameterTO = scenario.getParameters().get(BAR_LOAD);
        ParameterTO barStrengthParameterTO = scenario.getParameters().get(BAR_STRENGTH);

        Double seed = scenario.getConfigurations().get(SEED);


        RandomDataGenerator ran = new RandomDataGenerator();
        RandomGenerator randomGenerator = ran.getRandomGenerator();
        randomGenerator.setSeed(seed.longValue());


        this.loadDistributionMap(BAR_LOAD, barLoadParameterTO, randomGenerator);
        this.loadDistributionMap(BAR_STRENGTH, barStrengthParameterTO, randomGenerator);


        Double precision = scenario.getConfigurations().get(PRECISION);
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
            Map<String, Object> output = new HashMap<String, Object>();
            scenario.setOutput(output);
        }
        scenario.getOutput().put(FAILURE_PROBABILITY, prob);


        return scenario;*/
       return null;
    }

    private Double simulate(String key, ParameterTO variable) {
        if (ValueType.VARIABLE.equals(variable.getType())) {
            //variable.setValue(distributionMap.get(key).sample());
            ParameterTO p = new ParameterTO();
            p.setUnit(variable.getUnit());
            p.setType(variable.getType());
            p.setValue(distributionMap.get(key).sample());

            return loadAndNormalize(p).getValue();
        }

        return loadAndNormalize(variable).getValue();
    }

    private void loadDistributionMap(String key, ParameterTO variable, RandomGenerator randomGenerator) {
        switch (variable.getDistributionTO().getType()) {
            case DistributionType.NORMAL:
                Double variance = variable.getDistributionTO().getParameters().get(VARIANCE);
                Double mean = variable.getValue();
                distributionMap.put(key, new NormalDistribution(randomGenerator, mean, variance));
                break;
            case DistributionType.LOGNORMAL:
                Double scale = variable.getDistributionTO().getParameters().get(SCALE);
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
            case UnitSystem.INTERNATIONAL_SYSTEM :
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
            case UnitSystem.US_SYSTEM:
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
