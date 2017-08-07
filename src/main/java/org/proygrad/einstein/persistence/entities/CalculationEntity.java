package org.proygrad.einstein.persistence.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "CALCULATIONS")
public class CalculationEntity extends AbstractHibernateEntity<UUID>{

    private UUID id;


    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID",columnDefinition = "BINARY(16)")
    @Id
    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }
}
