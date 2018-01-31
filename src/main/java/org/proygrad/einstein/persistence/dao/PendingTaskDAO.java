package org.proygrad.einstein.persistence.dao;

import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PendingTaskDAO  extends AbstractHibernateEntityDAO<PendingTaskEntity,String> {


    private static final String RUNNING = "running";


    public List<PendingTaskEntity> getPendingTask(int maxLimitQuery) {


        EntityManager em = getCurrentSession();

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<PendingTaskEntity> cq = cb.createQuery(PendingTaskEntity.class);

        Root<PendingTaskEntity> from = cq.from(PendingTaskEntity.class);
        cq.select(from);

        Predicate c1 = cb.equal(from.get(RUNNING), false);

        Predicate condition = cb.and(c1);

        cq.where(condition);


        TypedQuery<PendingTaskEntity> query = em.createQuery(cq);
        return query.setMaxResults(maxLimitQuery).getResultList();

    }

}
