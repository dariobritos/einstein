package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.api.Parameter;
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
    private CalculationTO.UnitSystem unitSystem;

    public CalculationTO calculate(CalculationTO calculationTO) {

        calculationTO = calculateSimple(calculationTO);

        return calculationTO;
    }

    public CalculationTO calculateSimple(CalculationTO calculationTO) {

        unitSystem = calculationTO.getUnit();
        Parameter barLoadParameter = calculationTO.getParameters().get(BAR_LOAD);
        Parameter barStrengthParameter = calculationTO.getParameters().get(BAR_STRENGTH);

        Double seed = calculationTO.getConfigurations().get(SEED);


        RandomDataGenerator ran = new RandomDataGenerator();
        RandomGenerator randomGenerator = ran.getRandomGenerator();
        randomGenerator.setSeed(seed.longValue());


        this.loadDistributionMap(BAR_LOAD, barLoadParameter, randomGenerator);
        this.loadDistributionMap(BAR_STRENGTH, barStrengthParameter, randomGenerator);


        Double precision = calculationTO.getConfigurations().get(PRECISION);
        Integer failCount = 0;

        for (double i = 0; i < precision; i++) {

            Double barLoadSim = simulate(BAR_LOAD, barLoadParameter);
            Double barStrengthSim = simulate(BAR_STRENGTH, barStrengthParameter);

            if (barLoadSim > barStrengthSim) {
                failCount++;
            }
        }


        Double prob = failCount / precision;


        if (calculationTO.getOutput() == null) {
            Map<String, Object> output = new HashMap<String, Object>();
            calculationTO.setOutput(output);
        }
        calculationTO.getOutput().put(FAILURE_PROBABILITY, prob);


        return calculationTO;
    }

    private Double simulate(String key, Parameter variable) {
        if (Parameter.ValueType.VARIABLE.equals(variable.getType())) {
            //variable.setValue(distributionMap.get(key).sample());
            Parameter p = new Parameter();
            p.setUnit(variable.getUnit());
            p.setType(variable.getType());
            p.setValue(distributionMap.get(key).sample());

            return loadAndNormalize(p).getValue();
        }

        return loadAndNormalize(variable).getValue();
    }

    private void loadDistributionMap(String key, Parameter variable, RandomGenerator randomGenerator) {
        switch (variable.getDistribution().getType()) {
            case NORMAL:
                Double variance = variable.getDistribution().getParameters().get(VARIANCE);
                Double mean = variable.getValue();
                distributionMap.put(key, new NormalDistribution(randomGenerator, mean, variance));
                break;
            case LOGNORMAL:
                Double scale = variable.getDistribution().getParameters().get(SCALE);
                Double shape = variable.getValue();
                distributionMap.put(key, new LogNormalDistribution(randomGenerator, scale, shape));
                break;
            case POISSON:
                //TODO: VER DE AGREGAR! PARA MAS FUNCIONABILIDAD.
                break;
        }
    }


    //TODO: completar segun se necesite!!!
    private Parameter loadAndNormalize(Parameter variable) {
        switch (unitSystem) {
            case INTERNATIONAL_SYSTEM:
                switch (variable.getUnit()) {
                    case CENTIMETRE:
                        variable.setValue(variable.getValue() / 10);
                        variable.setUnit(Parameter.UnitType.MILLIMETRE);
                        break;
                    case KILOPASCAL:
                        variable.setValue(variable.getValue() * 1000);
                        variable.setUnit(Parameter.UnitType.MEGAPASCAL);
                        break;
                }
                break;
            case US_SYSTEM:
                switch (variable.getUnit()) {
                    case THOU:
                        variable.setValue(variable.getValue() * 0.0254);
                        variable.setUnit(Parameter.UnitType.MILLIMETRE);
                        break;
                    case INCH:
                        variable.setValue(variable.getValue() * 25.4);
                        variable.setUnit(Parameter.UnitType.MILLIMETRE);
                        break;
                    case FOOT:
                        variable.setValue(variable.getValue() * 304.8);
                        variable.setUnit(Parameter.UnitType.MILLIMETRE);
                        break;
                }
                break;
        }
        return variable;
    }
}
