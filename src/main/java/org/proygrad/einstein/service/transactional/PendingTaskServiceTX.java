package org.proygrad.einstein.service.transactional;

import org.proygrad.einstein.persistence.dao.PendingTaskDAO;
import org.proygrad.einstein.persistence.entities.PendingTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class PendingTaskServiceTX {

    private static final String LIMIT = "limit.query.pending.task";

    @Autowired
    private PendingTaskDAO pendingTaskDAO;

    @Autowired
    private Environment env;

    @Transactional
    public List<PendingTaskEntity> getPendingTask(){
        List<PendingTaskEntity> request = pendingTaskDAO.getPendingTask(Integer.parseInt(env.getRequiredProperty(LIMIT)));

        return request;
    }


    public void setCompleteTask(UUID entityId, boolean isRunning) {
        PendingTaskEntity toSave = pendingTaskDAO.load(entityId);
        toSave.setRunning(isRunning);

        // de seguro este de mas!
       // pendingTaskDAO.save(toSave);
    }
}
