package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.CommonItemTO;
import org.proygrad.einstein.api.ParameterTO;
import org.proygrad.einstein.api.ScenarioTO;
import org.proygrad.einstein.util.CommonItemUtil;
import org.proygrad.einstein.util.ParameterUtil;
import org.proygrad.einstein.util.UnitSystem;
import org.proygrad.einstein.util.UnitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateSeSurfaceCrackStraightPipe {

    //@Autowired
    //private CalculateTxtSupport calculateTxtSupport;

    @Autowired
    private ProbabilityDistribution probabilityDistribution;

    //NAME OF PARAMETERS ENTERED BY THE USER
    private static final String CRACK_DEPTH = "CRACK_DEPTH";
    private static final String CRACK_LENGTH = "CRACK_LENGTH";
    private static final String WALL_THICKNESS = "WALL_THICKNESS";
    private static final String FRACTURE_TOUGHNESS = "FRACTURE_TOUGHNESS";
    private static final String INNER_RADIUS = "INNER_RADIUS";
    private static final String YIELD_STRESS = "YIELD_STRESS";
    private static final String OPERATING_PRESSURE = "OPERATING_PRESSURE";

    private static final String SEED = "SEED";
    private static final String PRECISION = "PRECISION";

    //PROPERTY OF THE MATERIAL
    private static final String PLASTIC_COLLAPSE = "PLASTIC_COLLAPSE";
    private static final String FAILURE_PROBABILITY = "FAILURE_PROBABILITY";


    public ScenarioTO calculate(ScenarioTO scenario) {

        String unitSystem = scenario.getUnitSystem();
        //Obtener variables de entrada

        ParameterTO crackDepth = ParameterUtil.getParameter(scenario.getParameters(), CRACK_DEPTH); // a
        ParameterTO crackLength = ParameterUtil.getParameter(scenario.getParameters(), CRACK_LENGTH); // c
        ParameterTO wall_thickness = ParameterUtil.getParameter(scenario.getParameters(), WALL_THICKNESS); // t
        ParameterTO fractureToughness = ParameterUtil.getParameter(scenario.getParameters(), FRACTURE_TOUGHNESS); // KIC
        ParameterTO inner_radius = ParameterUtil.getParameter(scenario.getParameters(), INNER_RADIUS); // Ri
        ParameterTO yieldStress = ParameterUtil.getParameter(scenario.getParameters(), YIELD_STRESS); // SigS
        ParameterTO operatingPressure = ParameterUtil.getParameter(scenario.getParameters(), OPERATING_PRESSURE); // P

        ParameterTO plasticCollapse = ParameterUtil.getParameter(scenario.getParameters(), PLASTIC_COLLAPSE);  //scenario.getMaterials().get(); // LrMax
        Double LrMax = plasticCollapse.getValue();  // Plastic Collapse

        Double seed = CommonItemUtil.getValue(scenario.getConfiguration(), SEED);

        RandomDataGenerator ran = new RandomDataGenerator();
        RandomGenerator randomGenerator = ran.getRandomGenerator();
        randomGenerator.setSeed(seed.longValue());

        this.probabilityDistribution.loadDistributionMap(CRACK_DEPTH, crackDepth, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(CRACK_LENGTH, crackLength, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(WALL_THICKNESS, wall_thickness, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(FRACTURE_TOUGHNESS, fractureToughness, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(INNER_RADIUS, inner_radius, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(YIELD_STRESS, yieldStress, randomGenerator);
        this.probabilityDistribution.loadDistributionMap(OPERATING_PRESSURE, operatingPressure, randomGenerator);

        // N esta relacionado a la precision pedida.

        Double N = CommonItemUtil.getValue(scenario.getConfiguration(), PRECISION);
        Double n = 0d; // failure occur
        Double i = 0d; // iteration

        do {
            //Step 1 - Random variables
            Double a = loadAndNormalize(this.probabilityDistribution.simulatePositive(CRACK_DEPTH, crackDepth), unitSystem);
            Double c2 = loadAndNormalize(this.probabilityDistribution.simulatePositive(CRACK_LENGTH, crackLength), unitSystem);
            Double c = c2 / 2;
            Double t = loadAndNormalize(this.probabilityDistribution.simulatePositive(WALL_THICKNESS, wall_thickness), unitSystem);
            Double KIC = loadAndNormalize(this.probabilityDistribution.simulatePositive(FRACTURE_TOUGHNESS, fractureToughness), unitSystem);
            Double Ri = loadAndNormalize(this.probabilityDistribution.simulatePositive(INNER_RADIUS, inner_radius), unitSystem);
            Double SigS = loadAndNormalize(this.probabilityDistribution.simulatePositive(YIELD_STRESS, yieldStress), unitSystem);
            Double P = loadAndNormalize(this.probabilityDistribution.simulatePositive(OPERATING_PRESSURE, operatingPressure), unitSystem);

            boolean safePipe = true;

            if (validateParameters(a, c, t, KIC, Ri, SigS, P)) {

                Double Pm = (P * Ri) / t;

                Double Ro = Ri + t;
                Double Ro2 = Math.pow(Ro, 2);
                Double Ri2 = Math.pow(Ri, 2);

                Double Pb = ((P * Ro2) / (Ro2 - Ri2)) * ((t / Ri) - (1.5 * (Math.pow(t / Ri, 2))) + ((9 / 5) * (Math.pow(t / Ri, 3))));

                //Step 2 - Calculate toughness ratio Kr
                Double Kr = calculateKr(a, c, t, KIC, Pm, Pb);

                //Step 3 - Calculate load ratio Lr
                Double Lr = calculateLr(a, c, t, Ri, Pm, Pb, SigS);

                //Step 4 - Determine the position of (Kr, Lr)
                safePipe = isSafeZone(Kr, Lr, LrMax);
            }

            if (!safePipe) {
                n++;
            }
            i++;
        } while (i < N);

        //Step 5 - Determine the failure probability
        //1/N por Sumatoria de hj de 1 a N

        Double prob = n / N;

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

    private boolean validateParameters(Double a, Double c, Double t, Double KIC, Double Ri, Double SigS, Double P) {

        return a > 0 && c > 0 && t > 0 && KIC > 0 && Ri > 0 && SigS > 0 && P > 0;

    }

    private Double calculateKr(Double a, Double c, Double t, Double KIC, Double Pm, Double Pb ) {

        Double adivc = a / c;
        Double adivt = a / t;
        Double powAdivt2 = Math.pow(adivt, 2);
        Double aInMetres = a / 1000;

        Double fm0 = (1 / Math.pow(1 + (1.464 * (Math.pow((adivc), (1.65)))), 0.5));
        Double fm1 = (1.13 - (0.09 * adivc));
        Double fm2 = (-0.45 + (0.89 / (0.2 + adivc))) * powAdivt2;
        Double fm3 = (0.5 - (1 / (0.65 + adivc)) + (14 * Math.pow((1 - adivc), 24))) * Math.pow(adivt, 4);
        Double fm = fm0 * (fm1 + fm2 + fm3);

        Double fb0 = 1d;
        Double fb1 = (-1.22 - (0.12 * adivc)) * adivt;
        Double fb2 = (0.55 - (1.05 * (Math.pow(adivc, 0.75))) + (0.47 * (Math.pow(adivc, 1.5)))) * powAdivt2;
        Double fb = (fb0 + fb1 + fb2) * fm;

        Double KI = Math.sqrt(Math.PI * aInMetres) * ((Pm * fm) + (Pb * fb));

        return KI / KIC;
    }

    private Double calculateLr(Double a, Double c, Double t, Double Ri, Double Pm, Double Pb, Double SigS) {

        Double adivt = a / t;
        Double alpha = adivt / (1 + (t / c));

        Double Mt = Math.sqrt(1 + 1.6 * (Math.pow(c, 2) / (Ri * t)));
        Double Ms = (1 - (a / (t * Mt))) / (1 - (adivt));

        Double SigRef = (1.2 * Ms * Pm) + (2 * Pb) / (3 * Math.pow((1 - alpha), 2));

        return SigRef / SigS;
    }


    private boolean isSafeZone(Double Kr, Double Lr, Double LrMax) {

        // g(X)= f(Lr)-Kr
        Double gx = 0d;

        // f(Lr) = 0, si Lr > Lr max
        Double fLr = 0d;

        if (Lr <= LrMax) {
            Double Lr2 = Math.pow(Lr, 2);
            Double Lr6 = Math.pow(Lr, 6);
            Double partLr6 = -0.065 * Lr6;
            Double expLr6 = Math.exp(partLr6);

            fLr = (1 - (0.14 * Lr2)) * (0.3 + (0.7 * expLr6));
        }

        gx = fLr - Kr;

        return gx > 0; // the pipe safe!!!
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
