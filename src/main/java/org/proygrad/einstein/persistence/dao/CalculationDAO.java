package org.proygrad.einstein.persistence.dao;


import org.proygrad.einstein.persistence.AbstractHibernateEntityDAO;
import org.proygrad.einstein.persistence.entities.CalculationEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CalculationDAO extends AbstractHibernateEntityDAO<CalculationEntity,UUID> {
}
