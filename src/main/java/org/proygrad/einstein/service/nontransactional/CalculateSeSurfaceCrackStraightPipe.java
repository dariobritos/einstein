package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.api.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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


    public CalculationTO calculate(CalculationTO calculationTO) {

        //TODO: Asignar ejecutores, ver hilos, memoria compartida etc.
        //TODO: Mandar a ejecutar.
        //TODO: Esperar fin.
        //TODO: Acumular resultados parciales.
        //TODO: Armar retorno.

        calculationTO = calculateSimple(calculationTO);

        return calculationTO;
    }

    public CalculationTO calculateSimple(CalculationTO calculationTO) {

        CalculationTO.UnitSystem unitSystem = calculationTO.getUnit();
        //Obtener variables de entrada
        Parameter crackDepth = calculationTO.getParameters().get(CRACK_DEPTH); // a
        Parameter crackLength = calculationTO.getParameters().get(CRACK_LENGTH); // c
        Parameter wall_thickness = calculationTO.getParameters().get(WALL_THICKNESS); // t
        Parameter fractureToughness = calculationTO.getParameters().get(FRACTURE_TOUGHNESS); // KIC
        Parameter inner_radius = calculationTO.getParameters().get(INNER_RADIUS); // Ri
        Parameter yieldStress = calculationTO.getParameters().get(YIELD_STRESS); // SigS
        Parameter operatingPressure = calculationTO.getParameters().get(OPERATING_PRESSURE); // P

        Parameter plasticCollapse = calculationTO.getMaterials().get(PLASTIC_COLLAPSE); // LrMax
        Double LrMax = plasticCollapse.getValue();  // Plastic Collapse

        Double seed = calculationTO.getConfigurations().get(SEED);

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
        Double N = calculationTO.getConfigurations().get(PRECISION);
        Double n = 0d; // failure occur
        Double i = 0d; // iteration


        do {
            //Step 1 - Random variables
            Double a = this.probabilityDistribution.simulate(CRACK_DEPTH, crackDepth, unitSystem);
            Double c = this.probabilityDistribution.simulate(CRACK_LENGTH, crackLength, unitSystem);
            Double t = this.probabilityDistribution.simulate(WALL_THICKNESS, wall_thickness, unitSystem);
            Double KIC = this.probabilityDistribution.simulate(FRACTURE_TOUGHNESS, fractureToughness, unitSystem);
            Double Ri = this.probabilityDistribution.simulate(INNER_RADIUS, inner_radius, unitSystem);
            Double SigS = this.probabilityDistribution.simulate(YIELD_STRESS, yieldStress, unitSystem);
            Double P = this.probabilityDistribution.simulate(OPERATING_PRESSURE, operatingPressure, unitSystem);


            Double PRi = P * Ri;

            //calculateTxtSupport.mostrarCalculateOrig(a, c, t, KIC, PRi, SigS, P);

            //Step 2 - Calculate toughness ratio Kr
            Double Kr = calculateKr(a, c, t, KIC, PRi, SigS);

            //calculateTxtSupport.mostrarCalculateKr(a, c, t, KIC, PRi, SigS, Kr);

            //Step 3 - Calculate load ratio Lr
            Double Lr = calculateLr(a, c, t, Ri, PRi, P, SigS);

            //calculateTxtSupport.mostrarCalculateLr(a, c, t, Ri, PRi, P, SigS, Lr);

            //Step 4 - Determine the position of (Kr, Lr)
            boolean safePipe = isSafeZone(Kr, Lr, LrMax);
            if (!safePipe) {
                n++;
            }
            i++;
        } while (i < N);

        //Step 5 - Determine the failure probability
        //1/N por Sumatoria de hj de 1 a N

        if (calculationTO.getOutput() == null) {
            Map<String, Object> output = new HashMap<String, Object>();
            calculationTO.setOutput(output);
        }
        calculationTO.getOutput().put(FAILURE_PROBABILITY, n / N);
        calculationTO.getOutput().put("n", n);
        calculationTO.getOutput().put("N", N);
        calculationTO.getOutput().put("LrMax", LrMax);
        calculationTO.getOutput().put("i", i);

        return calculationTO;
    }

    private Double calculateKr(Double a, Double c, Double t, Double KIC, Double PRi, Double SigS) {

        //TODO: c nunca 0
        //TODO: t nunca 0

        Double adivc = a / c;
        Double adivt = a / t;
        Double powAdivt2 = Math.pow(adivt, 2);


        Double SigM = PRi / t;

        Double fm0 = (1 / Math.pow(1 + (1.464 * (Math.pow((adivc), (1.65)))), 0.5));
        Double fm1 = (1.13 - (0.09 * adivc));
        Double fm2 = (-0.45 + (0.89 / (0.2 + adivc))) * powAdivt2;
        Double fm3 = (0.5 - (1 / (0.65 + adivc)) + (14 * Math.pow((1 - adivc), 24))) * Math.pow(adivt, 4);
        Double fm = fm0 * (fm1 + fm2 + fm3);

        Double fb0 = 1d;
        Double fb1 = (-1.22 - (0.12 * adivc)) * adivt;
        Double fb2 = (0.55 - (1.05 * (Math.pow(adivc, 0.75))) + (0.47 * (Math.pow(adivc, 1.5)))) * powAdivt2;
        Double fb = (fb0 + fb1 + fb2) * fm;

        Double KI = Math.sqrt(Math.PI * a * ((SigM * fm) + (SigS * fb)));

        //calculateTxtSupport.mostrarCalculateKrDentro(adivc, adivt, powAdivt2, SigM, fm0,fm1,fm2,fm3,fm, fb1,fb2,fb, KI);

        return KI / KIC;
    }

    private Double calculateLr(Double a, Double c, Double t, Double Ri, Double PRi, Double P, Double SigS) {

        Double adivt = a / t;
        Double alpha = adivt / (1 + (t / c));

        Double Mt = Math.sqrt(1 + 1.6 * (Math.pow(c, 2) / (Ri * t)));
        Double Ms = (1 - (a / (t * Mt))) / (1 - (adivt));

        Double Pm = PRi / t;

        Double SigRef = (1.2 * Ms * Pm) + (2 * P) / (3 * Math.pow((1 - alpha), 2));

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

           // calculateTxtSupport.mostrarCalculateSafeZoneIf(Lr, Lr2, Lr6, expLr6, fLr);
        }

        gx = fLr - Kr;
       // calculateTxtSupport.mostrarIsSafeZone(gx, fLr, Kr, Lr);

        return gx > 0; // the pipe safe!!!
        // else failure occurs.
    }

}
