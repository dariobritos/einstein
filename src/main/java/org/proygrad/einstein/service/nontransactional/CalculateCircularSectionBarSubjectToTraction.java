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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculateCircularSectionBarSubjectToTraction {


    private static final String BAR_LOAD = "BAR_LOAD";
    private static final String YIELD_STRESS = "YIELD_STRESS";
    private static final String BAR_DIAMETER = "BAR_DIAMETER";

    private static final String SEED = "SEED";
    private static final String PRECISION = "PRECISION";

    private static final String FAILURE_PROBABILITY = "FAILURE_PROBABILITY";

    private static final String VARIANCE = "VARIANCE";
    private static final String SCALE = "SCALE";


    private Map<String, AbstractRealDistribution> distributionMap = new HashMap<String, AbstractRealDistribution>();

    @Autowired
    private ProbabilityDistribution probabilityDistribution;

    public ScenarioTO calculate(ScenarioTO scenario) {

        String unitSystem = scenario.getUnitSystem();

        ParameterTO barLoadParameterTO = ParameterUtil.getParameter(scenario.getParameters(), BAR_LOAD);
        ParameterTO barStrengthParameterTO = ParameterUtil.getParameter(scenario.getParameters(), YIELD_STRESS);
        ParameterTO barDiameterParameterTO = ParameterUtil.getParameter(scenario.getParameters(), BAR_DIAMETER);

        Double barDiameter = loadAndNormalize(barDiameterParameterTO, unitSystem);

        Double seed = CommonItemUtil.getValue(scenario.getConfiguration(), SEED);

        RandomDataGenerator ran = new RandomDataGenerator();
        RandomGenerator randomGenerator = ran.getRandomGenerator();
        randomGenerator.setSeed(seed.longValue());

        this.probabilityDistribution.loadDistributionMap(BAR_LOAD, barLoadParameterTO, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(YIELD_STRESS, barStrengthParameterTO, randomGenerator);


        Double precision = CommonItemUtil.getValue(scenario.getConfiguration(), PRECISION);
        Integer failCount = 0;

        Double radio = barDiameter/2;
        Double area = Math.PI * radio * radio  ;

        for (double i = 0; i < precision; i++) {


            Double barLoadSim = loadAndNormalize(this.probabilityDistribution.simulatePositive(BAR_LOAD, barLoadParameterTO), unitSystem);
            Double barStrengthSim = loadAndNormalize(this.probabilityDistribution.simulatePositive(YIELD_STRESS, barStrengthParameterTO), unitSystem);

            Double barLoadWork = barLoadSim/area;

            if (!(barLoadWork > barStrengthSim)) {
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
    //TODO: luego de sacar a la clase concreta me da la interrogante de agregar algo mas por q lo normalizado para un calculo no es lo mismo para otro.
    // en este serian milimetros y megapascales.
    private Double loadAndNormalize(ParameterTO variable, String unitSystem) {
        switch (unitSystem) {
            case UnitSystem.INTERNATIONAL:
                switch (variable.getUnit()) {
                    case UnitType.METER:
                        variable.setValue(variable.getValue() * 1000);
                        variable.setUnit(UnitType.MILLIMETER);
                        break;
                    case UnitType.CENTIMETER:
                        variable.setValue(variable.getValue() * 10);
                        variable.setUnit(UnitType.MILLIMETER);
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
                        variable.setUnit(UnitType.MILLIMETER);
                        break;
                    case UnitType.INCH:
                        variable.setValue(variable.getValue() * 25.4);
                        variable.setUnit(UnitType.MILLIMETER);
                        break;
                    case UnitType.FOOT:
                        variable.setValue(variable.getValue() * 304.8);
                        variable.setUnit(UnitType.MILLIMETER);
                        break;
                    case UnitType.NW_MM2:
                        variable.setValue(variable.getValue());
                        variable.setUnit(UnitType.MEGAPASCAL);
                        break;
                    case UnitType.NW_CM2:
                        variable.setValue(variable.getValue() * 0.01);
                        variable.setUnit(UnitType.MEGAPASCAL);
                        break;
                }
                break;
        }
        return variable.getValue();
    }
}
