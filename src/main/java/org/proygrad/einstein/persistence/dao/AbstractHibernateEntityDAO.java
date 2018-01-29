package org.proygrad.einstein.persistence.dao;


import org.proygrad.einstein.persistence.entities.AbstractHibernateEntity;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

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


    public T read(ID id) {
        return this.getCurrentSession().get(this.clazz, id);
    }


    public T load(ID id) {
        T entity = this.read(id);
        if (entity == null) {
            throw new javax.persistence.EntityNotFoundException("Unable to find " + this.clazz.getSimpleName() + " with id #" + id);
        }
        return entity;
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
