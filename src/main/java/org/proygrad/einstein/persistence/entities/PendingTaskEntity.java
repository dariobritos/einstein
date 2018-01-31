package org.proygrad.einstein.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PENDING_TASK")
public class PendingTaskEntity extends AbstractHibernateEntity<String> {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REQUEST_ID")
    private String requestId;

    @Column(name = "CREATE_DATE")
     private Date createDate;

    @Column(name = "RUNNING")
    private Boolean running;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
    return requestId;
    }

    public void setRequestId(String requestId) {
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
