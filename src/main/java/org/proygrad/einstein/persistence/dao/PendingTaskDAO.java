package org.proygrad.einstein.persistence.dao;

import org.proygrad.einstein.persistence.AbstractHibernateEntityDAO;
import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PendingTaskDAO  extends AbstractHibernateEntityDAO<PendingTaskEntity,UUID> {

   /* public List<PendingTaskEntity> getPending() {
        List<PendingTaskEntity> entities = null;

        return entities;

    }*/
}
