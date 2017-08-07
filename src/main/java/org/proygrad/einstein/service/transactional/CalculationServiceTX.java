package org.proygrad.einstein.service.transactional;


import org.proygrad.einstein.api.CalculationTO;
import org.proygrad.einstein.persistence.dao.CalculationDAO;
import org.proygrad.einstein.persistence.entities.CalculationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculationServiceTX {

    @Autowired
    private CalculationDAO calculationDAO;

    public void addCalculation(CalculationTO calculationTO) {


        CalculationEntity entity = new CalculationEntity();


        calculationDAO.save(entity);


    }

    public List<CalculationTO> getCalculations() {

        return calculationDAO.readAll().stream().map(x->{
            CalculationTO to = new CalculationTO();
            to.setId(x.getId().toString());
            return to;
        }).collect(Collectors.toList());
    }
}
