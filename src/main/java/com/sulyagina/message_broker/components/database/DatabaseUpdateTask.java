package com.sulyagina.message_broker.components.database;

import com.sulyagina.message_broker.dao.AbstractDAO;

import java.io.Serializable;

/**
 * Created by anastasia on 30.05.16.
 */
public class DatabaseUpdateTask implements DatabaseTask {
    Entity entityType;
    Serializable object;

    public DatabaseUpdateTask(Entity entityType, Serializable object) {
        this.entityType = entityType;
        this.object = object;
    }

    @Override
    public void writeTo(AbstractDAO dao) {
        dao.update(object);
    }

    @Override
    public Entity getEntityType() {
        return entityType;
    }
}
