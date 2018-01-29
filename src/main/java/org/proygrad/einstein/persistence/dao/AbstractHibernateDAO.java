package org.proygrad.einstein.persistence.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("rawtypes")
public class AbstractHibernateDAO {

    @Autowired
    protected SessionFactory sessionFactory;

    protected final Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

}
