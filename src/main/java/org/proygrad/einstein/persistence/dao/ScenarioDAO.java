package org.proygrad.einstein.persistence.dao;


import org.proygrad.einstein.persistence.AbstractHibernateEntityDAO;
import org.proygrad.einstein.persistence.entities.ScenarioEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ScenarioDAO extends AbstractHibernateEntityDAO<ScenarioEntity,UUID> {

    public List<ScenarioEntity> getPending() {
        List<ScenarioEntity> entities = null;

        return entities;

    }
}
