package org.proygrad.einstein.persistence;


import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.proygrad.einstein.persistence.entities.AbstractHibernateEntity;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Provides a way to use annotations to load up DAOs.
 *
 * @param <T> the type of entity.
 * @param <ID> identifier of an entity.
 */
public abstract class AbstractHibernateEntityDAO<T extends AbstractHibernateEntity<ID>, ID extends Serializable>
                extends AbstractHibernateDAO {

    protected Class<T> clazz;

    @PostConstruct
    @SuppressWarnings("unchecked")
    protected void loadGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = ((Class<T>) parameterizedType.getActualTypeArguments()[0]);
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    protected DetachedCriteria createCriteria(String classAlias) {
        return DetachedCriteria.forClass(this.clazz, classAlias == null ? this.clazz.getSimpleName().toLowerCase() : classAlias);
    }

    public T read(ID id) {
        return this.getCurrentSession().get(this.clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> readAll() {
        return this.getSession().list();
    }

    public T load(ID id) {
        T entity = this.read(id);
        if (entity == null) {
            throw new javax.persistence.EntityNotFoundException("Unable to find " + this.clazz.getSimpleName() + " with id #" + id);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByProperty(String property, Object value) {
        return this.getSession().add(Restrictions.eq(property, value)).list();
    }


    @SuppressWarnings("unchecked")
    protected List<T> findByProperty(String property, Object value, int maxResults) {
        return this.getSession().add(Restrictions.eq(property, value)).setMaxResults(maxResults).list();
    }

    @SuppressWarnings("unchecked")
    protected List<T> findByProperties(Map<String, Object> properties) {
        Criteria criteria = this.getSession();
        for (Entry<String, Object> property : properties.entrySet()) {
            criteria = criteria.add(Restrictions.eq(property.getKey(), property.getValue()));
        }
        return criteria.list();
    }

    protected Criteria join(String associationPath, String alias, JoinType joinType) {
        Criteria criteria = this.getSession();
        criteria.createAlias(associationPath, alias, joinType);
        return criteria;
    }

    protected Criteria getSession() {
        return this.getCurrentSession().createCriteria(this.clazz);
    }

    protected T readByProperty(String property, Object value) {
        List<T> result = this.findByProperty(property, value);
        return result.isEmpty() ? null : result.get(0);
    }

    protected T loadByProperty(String property, Object value) {
        List<T> result = this.findByProperty(property, value);
        this.validateUnique(result);
        return result.isEmpty() ? null : result.get(0);
    }

    protected T readByProperties(Map<String, Object> properties) {
        List<T> result = this.findByProperties(properties);
        this.validateUnique(result);
        return result.isEmpty() ? null : result.get(0);
    }

    protected T loadByProperties(Map<String, Object> properties) {
        List<T> result = this.findByProperties(properties);
        if (result.isEmpty()) {
            StringBuilder sb = new StringBuilder("Unable to find ");
            sb.append(this.clazz.getSimpleName()).append("with properties: ");
            for (Entry<String, Object> property : properties.entrySet()) {
                sb.append(" - ").append(property.getKey()).append(": ").append(properties.get(property.getValue()));
            }
            throw new javax.persistence.EntityNotFoundException(sb.toString());
        }
        this.validateUnique(result);
        return result.get(0);
    }


    // Validations

    private void validateUnique(List<T> result) {
        if (result.size() > 1) {
            throw new javax.persistence.NonUniqueResultException("Expected single result, but got " + result);
        }
    }

    public void save(T entity) {
        this.getCurrentSession().saveOrUpdate(entity);
    }

    public void update(T entity) {
        if (entity.getId() == null) {
            throw new javax.persistence.PersistenceException("Update entity " + entity.getClass() + " without id");
        }
        this.getCurrentSession().saveOrUpdate(entity);
    }

    public void refresh(T entity) {
        this.getCurrentSession().refresh(entity);
    }

    public void merge(T entity) {
        this.getCurrentSession().merge(entity);
    }

    public void flush() {
        this.getCurrentSession().flush();
    }

    public void delete(T entity) {
        this.getCurrentSession().delete(entity);
    }

    public void delete(ID id) {
        T entity = this.load(id);
        this.getCurrentSession().delete(entity);
    }


}
