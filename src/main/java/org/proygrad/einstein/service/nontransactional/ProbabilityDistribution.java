package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.ParameterTO;
import org.proygrad.einstein.util.DistributionType;
import org.proygrad.einstein.util.ValueType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProbabilityDistribution {

    private static final String VARIANCE = "VARIANCE";
    private static final String SCALE = "SCALE";

    //distributionMap
    private Map<String, AbstractRealDistribution> distributionMap = new HashMap<String, AbstractRealDistribution>();

    public ParameterTO simulate(String key, ParameterTO variable) {
        if (ValueType.VARIABLE.equals(variable.getType())) {
            //variable.setValue(distributionMap.get(key).sample());
            ParameterTO p = new ParameterTO();
            p.setUnit(variable.getUnit());
            p.setType(variable.getType());
            p.setValue(distributionMap.get(key).sample());

            return p;
        }

        return  variable;
    }

    public void loadDistributionMap(String key, ParameterTO variable, RandomGenerator randomGenerator) {

        switch (variable.getDistributionTO().getType()) {
            case DistributionType.NORMAL:
                Double variance = variable.getDistributionTO().getParameters().get(VARIANCE);
                Double mean = variable.getValue();
                distributionMap.put(key, new NormalDistribution(randomGenerator, mean, variance));
                break;
            case DistributionType.LOGNORMAL:
                double m = variable.getValue();
                double v = Math.pow(variable.getDistributionTO().getParameters().get(SCALE),2);

                double mu = Math.log(Math.pow(m,2)/Math.sqrt(v+Math.pow(m,2)));
                double sigma = Math.sqrt(Math.log((v/(Math.pow(m,2)) + 1)));

                distributionMap.put(key, new LogNormalDistribution(randomGenerator, mu, sigma));
                break;
            case DistributionType.POISSON:
                //TODO: VER DE AGREGAR! PARA MAS FUNCIONABILIDAD.
                break;
        }
    }
}
