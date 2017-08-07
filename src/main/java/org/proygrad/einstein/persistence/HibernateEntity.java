package org.proygrad.einstein.persistence;

public interface HibernateEntity<ID> {

    ID getId();

    void setId(ID id);
}
