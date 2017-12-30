package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.api.Parameter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProbabilityDistribution {

    private static final String VARIANCE = "VARIANCE";
    private static final String SCALE = "SCALE";

    //distributionMap
    private Map<String, AbstractRealDistribution> distributionMap = new HashMap<String, AbstractRealDistribution>();

    public Double simulate(String key, Parameter variable, CalculationTO.UnitSystem unitSystem) {
        if (Parameter.ValueType.VARIABLE.equals(variable.getType())) {
            //variable.setValue(distributionMap.get(key).sample());
            Parameter p = new Parameter();
            p.setUnit(variable.getUnit());
            p.setType(variable.getType());
            p.setValue(distributionMap.get(key).sample());

            return loadAndNormalize(p, unitSystem).getValue();
        }

        return loadAndNormalize(variable, unitSystem).getValue();
    }

    public void loadDistributionMap(String key, Parameter variable, RandomGenerator randomGenerator) {

        switch (variable.getDistribution().getType()) {
            case NORMAL:
                Double variance = variable.getDistribution().getParameters().get(VARIANCE);
                Double mean = variable.getValue();
                distributionMap.put(key, new NormalDistribution(randomGenerator, mean, variance));
                break;
            case LOGNORMAL:
                double m = variable.getValue();
                double v = Math.pow(variable.getDistribution().getParameters().get(SCALE),2);

                double mu = Math.log(Math.pow(m,2)/Math.sqrt(v+Math.pow(m,2)));
                double sigma = Math.sqrt(Math.log((v/(Math.pow(m,2)) + 1)));

                distributionMap.put(key, new LogNormalDistribution(randomGenerator, mu, sigma));
                break;
            case POISSON:
                //TODO: VER DE AGREGAR! PARA MAS FUNCIONABILIDAD.
                break;
        }
    }


    //TODO: completar segun se necesite!!!
    //TODO: luego de sacar a la clase concreta me da la interrogante de agregar algo mas por q lo normalizado para un calculo no es lo mismo para otro.
    // en este serian milimetros y megapascales.
    private Parameter loadAndNormalize(Parameter variable, CalculationTO.UnitSystem unitSystem) {
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
