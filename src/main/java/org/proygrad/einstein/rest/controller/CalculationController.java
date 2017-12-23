package org.proygrad.einstein.rest.controller;

import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.api.Distribution;
import org.proygrad.einstein.api.Parameter;
import org.proygrad.einstein.service.nontransactional.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalculationController {


    @Autowired
    private CalculationService calculationService;

    @RequestMapping(value = "/calculation", method = RequestMethod.GET)
    public List<CalculationTO> calculation() {
        return calculationService.getCalculations();
    }

    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    public void calculationAdd(@RequestBody CalculationTO calculationTO) {

        calculationService.addCalculation(calculationTO);
    }

    @RequestMapping(value = "/calculation/resolve", method = RequestMethod.POST)
    public CalculationTO calculationResolve(@RequestBody CalculationTO calculationTO) {

        calculationTO=generateCalculationMock(calculationTO);

        return calculationService.calculationResolve(calculationTO);
    }

    private CalculationTO generateCalculationMock(CalculationTO calculationTO) {
        //TODO: MOCKEAR LA ENTRADA
        Map<String, Parameter> parameters = new HashMap<String, Parameter>();
        Map<String, Parameter> materials = new HashMap<String, Parameter>();
        Map<String, Double> configurations = new HashMap<String, Double>();

        calculationTO.setId("ID-123-456-789");
        calculationTO.setType("SE_SURFACE_CRACK_STRAIGHT_PIPE");
        calculationTO.setUnit(CalculationTO.UnitSystem.INTERNATIONAL_SYSTEM);

        Parameter crackDepth = new Parameter();
        Map<String, Double> parametersDistribution  = new HashMap<String, Double>();
        parametersDistribution.put("VARIANCE", 0.5d);
        Distribution crackDepthDistribution = new Distribution();
        crackDepthDistribution.setParameters(parametersDistribution);
        crackDepthDistribution.setType(Distribution.DistributionType.NORMAL);
        crackDepth.setValue(5d);
        crackDepth.setType(Parameter.ValueType.VARIABLE);
        crackDepth.setDistribution(crackDepthDistribution);
        crackDepth.setUnit(Parameter.UnitType.MILLIMETRE);

        parameters.put("CRACK_DEPTH", crackDepth);

        Parameter crackLength = new Parameter();
        Map<String, Double> crackLengthParametersDistribution  = new HashMap<String, Double>();
        crackLengthParametersDistribution.put("VARIANCE", 8d);
        Distribution crackLengthDistribution = new Distribution();
        crackLengthDistribution.setParameters(crackLengthParametersDistribution);
        crackLengthDistribution.setType(Distribution.DistributionType.NORMAL);
        crackLength.setValue(80d);
        crackLength.setType(Parameter.ValueType.VARIABLE);
        crackLength.setDistribution(crackLengthDistribution);
        crackLength.setUnit(Parameter.UnitType.MILLIMETRE);

        parameters.put("CRACK_LENGTH", crackLength);

        Parameter wallThickness = new Parameter();
        Map<String, Double> wallThicknessParametersDistribution  = new HashMap<String, Double>();
        wallThicknessParametersDistribution.put("VARIANCE", 2.5d);
        Distribution wallThicknessDistribution = new Distribution();
        wallThicknessDistribution.setParameters(wallThicknessParametersDistribution);
        wallThicknessDistribution.setType(Distribution.DistributionType.NORMAL);
        wallThickness.setValue(25d);
        wallThickness.setType(Parameter.ValueType.VARIABLE);
        wallThickness.setDistribution(wallThicknessDistribution);
        wallThickness.setUnit(Parameter.UnitType.MILLIMETRE);

        parameters.put("WALL_THICKNESS", wallThickness);

        Parameter fractureToughness = new Parameter();
        Map<String, Double> fractureToughnessParametersDistribution  = new HashMap<String, Double>();
        fractureToughnessParametersDistribution.put("SCALE", 32.97d);
        Distribution fractureToughnessDistribution = new Distribution();
        fractureToughnessDistribution.setParameters(fractureToughnessParametersDistribution);
        fractureToughnessDistribution.setType(Distribution.DistributionType.LOGNORMAL);
        fractureToughness.setValue(329.7d);
        fractureToughness.setType(Parameter.ValueType.VARIABLE);
        fractureToughness.setDistribution(fractureToughnessDistribution);
        fractureToughness.setUnit(Parameter.UnitType.MEGAPASCAL);

        parameters.put("FRACTURE_TOUGHNESS", fractureToughness);

        Parameter innerRadius = new Parameter();
        Map<String, Double> innerRadiusParametersDistribution  = new HashMap<String, Double>();
        innerRadiusParametersDistribution.put("VARIANCE", 150d);
        Distribution innerRadiusDistribution = new Distribution();
        innerRadiusDistribution.setParameters(innerRadiusParametersDistribution);
        innerRadiusDistribution.setType(Distribution.DistributionType.NORMAL);
        innerRadius.setValue(1500d);
        innerRadius.setType(Parameter.ValueType.VARIABLE);
        innerRadius.setDistribution(innerRadiusDistribution);
        innerRadius.setUnit(Parameter.UnitType.MILLIMETRE);

        parameters.put("INNER_RADIUS", innerRadius);

        Parameter yieldStress = new Parameter();
        Map<String, Double> yieldStressParametersDistribution  = new HashMap<String, Double>();
        yieldStressParametersDistribution.put("SCALE", 24.8d);
        Distribution yieldStressDistribution = new Distribution();
        yieldStressDistribution.setParameters(yieldStressParametersDistribution);
        yieldStressDistribution.setType(Distribution.DistributionType.LOGNORMAL);
        yieldStress.setValue(248d);
        yieldStress.setType(Parameter.ValueType.VARIABLE);
        yieldStress.setDistribution(yieldStressDistribution);
        yieldStress.setUnit(Parameter.UnitType.MEGAPASCAL);

        parameters.put("YIELD_STRESS", yieldStress);

        Parameter operatingPressure = new Parameter();
        Map<String, Double> operatingPressureParametersDistribution  = new HashMap<String, Double>();
        operatingPressureParametersDistribution.put("SCALE", 0.34d);
        Distribution operatingPressureDistribution = new Distribution();
        operatingPressureDistribution.setParameters(operatingPressureParametersDistribution);
        operatingPressureDistribution.setType(Distribution.DistributionType.LOGNORMAL);
        operatingPressure.setValue(3.4d);
        operatingPressure.setType(Parameter.ValueType.VARIABLE);
        operatingPressure.setDistribution(operatingPressureDistribution);
        operatingPressure.setUnit(Parameter.UnitType.MEGAPASCAL);

        parameters.put("OPERATING_PRESSURE", operatingPressure);
        calculationTO.setParameters(parameters);

        Parameter plasticCollapse = new Parameter();
        plasticCollapse.setValue(0.0007d);
        plasticCollapse.setType(Parameter.ValueType.STATIC);
        plasticCollapse.setDistribution(null);
        plasticCollapse.setUnit(Parameter.UnitType.MEGAPASCAL);

        materials.put("PLASTIC_COLLAPSE", plasticCollapse);

        calculationTO.setMaterials(materials);

        configurations.put("SEED", 123456d);
        configurations.put("PRECISION", Math.pow(10,3));
        calculationTO.setConfigurations(configurations);

        return calculationTO;
    }
}
