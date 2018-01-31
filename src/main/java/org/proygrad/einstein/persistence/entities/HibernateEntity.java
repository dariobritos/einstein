package org.proygrad.einstein.persistence.entities;

public interface HibernateEntity<ID> {

    ID getId();

    void setId(ID id);
}
