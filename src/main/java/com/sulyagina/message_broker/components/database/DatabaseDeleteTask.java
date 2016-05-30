package com.sulyagina.message_broker.components.database;

import com.sulyagina.message_broker.dao.AbstractDAO;

import java.io.Serializable;

/**
 * Created by anastasia on 30.05.16.
 */
public class DatabaseDeleteTask implements DatabaseTask {
    DatabaseTask.Entity entityType;
    Serializable object;

    public DatabaseDeleteTask(DatabaseTask.Entity entityType, Serializable object) {
        this.entityType = entityType;
        this.object = object;
    }

    @Override
    public void writeTo(AbstractDAO dao) {
        dao.delete(object);
    }

    @Override
    public DatabaseTask.Entity getEntityType() {
        return entityType;
    }
}
