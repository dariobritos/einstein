package org.proygrad.einstein.persistence.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "PENDING_TASK")
public class PendingTaskEntity extends AbstractHibernateEntity<UUID> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "ID")
    private UUID id;

    @Column(name = "REQUEST_ID")
    private UUID requestId;

    @Column(name = "CREATE_DATE")
     private Date createDate;

    @Column(name = "RUNNING")
    private Boolean running;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

     public Date getCreateDate() {
         return createDate;
     }

     public void setCreateDate(Date createDate) {
         this.createDate = createDate;
     }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

}
