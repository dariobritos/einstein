package org.proygrad.einstein.service.nontransactional;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.api.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateSeSurfaceCrackStraightPipe {

    @Autowired
    private CalculateTxtSupport calculateTxtSupport;

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

    private static final String VARIANCE = "VARIANCE";
    private static final String SCALE = "SCALE";


    private Map<String, AbstractRealDistribution> distributionMap = new HashMap<String, AbstractRealDistribution>();
    private CalculationTO.UnitSystem unitSystem;

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

        unitSystem = calculationTO.getUnit();
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

        this.loadDistributionMap(CRACK_DEPTH, crackDepth, seed);
        this.loadDistributionMap(CRACK_LENGTH, crackLength, seed);
        this.loadDistributionMap(WALL_THICKNESS, wall_thickness, seed);
        this.loadDistributionMap(FRACTURE_TOUGHNESS, fractureToughness, seed);
        this.loadDistributionMap(INNER_RADIUS, inner_radius, seed);
        this.loadDistributionMap(YIELD_STRESS, yieldStress, seed);
        this.loadDistributionMap(OPERATING_PRESSURE, operatingPressure, seed);

        // N esta relacionado a la precision pedida.
        Double N = calculationTO.getConfigurations().get(PRECISION);
        Double n = 0d; // failure occur
        Double i = 0d; // iteration


        do {
            //Step 1 - Random variables
            Double a = this.simulate(CRACK_DEPTH, crackDepth);
            Double c = this.simulate(CRACK_LENGTH, crackLength);
            Double t = this.simulate(WALL_THICKNESS, wall_thickness);
            Double KIC = this.simulate(FRACTURE_TOUGHNESS, fractureToughness);
            Double Ri = this.simulate(INNER_RADIUS, inner_radius);
            Double SigS = this.simulate(YIELD_STRESS, yieldStress);
            Double P = this.simulate(OPERATING_PRESSURE, operatingPressure);

            Double PRi = P * Ri;

            //Step 2 - Calculate toughness ratio Kr
            Double Kr = calculateKr(a, c, t, KIC, PRi, SigS);

            mostrarCalculateKr(a, c, t, KIC, PRi, SigS, Kr);

            //Step 3 - Calculate load ratio Lr
            Double Lr = calculateLr(a, c, t, Ri, PRi, P, SigS);

            mostrarCalculateLr(a, c, t, Ri, PRi, P, SigS, Lr);

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

        Double fm0 = (1 / Math.pow(1 + 1.464 * (Math.pow((adivc), (1.65))), 0.5));
        Double fm1 = (1.13 + 0.09 * adivc);
        Double fm2 = (-0.45 + (0.89 / (0.2 + adivc))) * powAdivt2;
        Double fm3 = (0.5 - (1 / (0.65 + adivc)) + 14 * Math.pow((1 - adivc), 24)) * Math.pow(adivt, 4);
        Double fm = fm0 * (fm1 + fm2 + fm3);

        Double fb0 = 1D;
        Double fb1 = (-1.22 - (0.12 * adivc)) * adivt;
        Double fb2 = (0.55 - (1.05 * (Math.pow(adivc, 0.75))) + (0.47 * (Math.pow(adivc, 15)))) * powAdivt2;
        Double fb = (fb0 + fb1 + fb2) * fm;

        Double KI = Math.sqrt(Math.PI * a) * (SigM * fm + SigS * fb);

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
        Double gx;

        // f(Lr) = 0, si Lr > Lr max
        Double fLr = 0d;

        // f(Lr) = (1-0,14(Lr)^2)*(0,3+0,7exp(-0,65(Lr)^6))
        if (Lr <= LrMax) {

            Double Lr2 = Math.pow(Lr, 2);
            Double Lr6 = Math.pow(Lr, 6);
            Double expLr6 = Math.exp(-0.065 * Lr6);


            Double p1=(1 - (0.14 * Lr2));
            Double p2=(0.3 + (0.7 * expLr6));
            Double p3=p1*p2;

            String line = "";
            line = line + "Lr2:" + Lr2.toString();
            line = line + ", Lr6:" + Lr6.toString();
            line = line + ", expLr6:" + expLr6.toString();
            line = line + ", p1:" + p1.toString();
            line = line + ", p2:" + p2.toString();
            line = line + ", p3:" + p3.toString();
            line = line + "\n";

            calculateTxtSupport.saveLineTxt("testFileIsSafeZoneIf", line);

            fLr = (1 - (0.14 * Lr2)) * (0.3 + (0.7 * expLr6));
        } else {
            calculateTxtSupport.saveLineTxt("testFileIsSafeZoneElse", "44444");
        }

        gx = fLr - Kr;


        mostrarIsSafeZone(gx, fLr, Kr, Lr);

        return gx > 0; // the pipe safe!!!
        // else failure occurs.
    }

    private void mostrarIsSafeZone(Double gx, Double fLr, Double  Kr, Double Lr ) {

        String line = "";
        line = line + "gx:" + gx.toString();
        line = line + ", fLr:" + fLr.toString();
        line = line + ", Kr:" + Kr.toString();
        line = line + ", Lr:" + Lr.toString();
        line = line + "\n";

        calculateTxtSupport.saveLineTxt("testFileIsSafeZone", line);
    }

    private void mostrarCalculateKr(Double a, Double c, Double t, Double KIC, Double PRi, Double SigS, Double Kr) {

        String line = "";
        line = line + "a:" + a.toString();
        line = line + ", c:" + c.toString();
        line = line + ", t:" + t.toString();
        line = line + ", KIC:" + KIC.toString();
        line = line + ", PRi:" + PRi.toString();
        line = line + ", SigS:" + SigS.toString();
        line = line + ", Kr:" + Kr.toString();
        line = line + "\n";

        calculateTxtSupport.saveLineTxt("testFileKr", line);
    }

    private void mostrarCalculateLr(Double a, Double c, Double t, Double ri, Double pRi, Double p, Double sigS, Double Lr) {

        String line = "";
        line = line + "a:" + a.toString();
        line = line + ", c:" + c.toString();
        line = line + ", t:" + t.toString();
        line = line + ", ri:" + ri.toString();
        line = line + ", pRi:" + pRi.toString();
        line = line + ", p:" + p.toString();
        line = line + ", sigS:" + sigS.toString();
        line = line + ", Lr:" + Lr.toString();
        line = line + "\n";

        calculateTxtSupport.saveLineTxt("testFileLr", line);
    }


// g(X)= f(Lr)-Kr
// f(Lr) = 0, si Lr> Lr max
// f(Lr) = (1-0,14(Lr)^2)*(0,3+0,7exp(-0,65(Lr)^6))

// Stress intensity factor (defect size - stress analysis)
// Ki
// fracture toughness
// Kic
// ----> Kr!a = Ki/Kic (! para indicar supraindice)

// Reference Stress (defect size - stress analysis)
// SIGref
// Yield Stress
// SIGs
// --------> Lr!a = SIGref/SIGs

// Pf(X)=P(g(X)<=0)
// Pf es la integral doble de P(Lr,Kr) en el area g(x)<=0.... como esto es pesado

// Pf=(1/N)* SUMATORIA de j=1..N (hj), N numero de ciclos de Monte Carlo
// hj= 1 si g(X)<=0
// hj= 0 si g(X)> 0

    //distributionMap

    private Double simulate(String key, Parameter variable) {
        if (Parameter.ValueType.VARIABLE.equals(variable.getType())) {
            variable.setValue(distributionMap.get(key).sample());
        }

        return loadAndNormalize(variable).getValue();
    }

    private void loadDistributionMap(String key, Parameter variable, Double seed) {
        switch (variable.getDistribution().getType()) {
            case NORMAL:
                Double variance = variable.getDistribution().getParameters().get(VARIANCE);
                Double mean = variable.getValue();
                RandomDataGenerator ran = new RandomDataGenerator();
                ran.getRandomGenerator().setSeed(seed.longValue());
                distributionMap.put(key, new NormalDistribution(ran.getRandomGenerator(), mean, variance));
                break;
            case LOGNORMAL:
                Double scale = variable.getDistribution().getParameters().get(SCALE);
                Double shape = variable.getValue();
                RandomDataGenerator ranL = new RandomDataGenerator();
                ranL.getRandomGenerator().setSeed(seed.longValue());
                distributionMap.put(key, new LogNormalDistribution(ranL.getRandomGenerator(), shape, scale));
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
