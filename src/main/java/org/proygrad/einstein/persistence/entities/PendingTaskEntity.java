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
    private boolean running;

    @Column(name = "COMPLETE")
    private boolean complete;

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

    public boolean getRunning() {
    return running;
    }

    public void setRunning(boolean running) {
    this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
