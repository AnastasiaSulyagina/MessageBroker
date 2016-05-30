package com.sulyagina.message_broker.components.database;

import com.sulyagina.message_broker.dao.AbstractDAO;

/**
 * Created by anastasia on 30.05.16.
 */
public interface DatabaseTask {
    public enum Entity{
        LISTENER,
        TOPIC,
        BROADCAST
    }
    void writeTo(AbstractDAO dao);
    Entity getEntityType();
}
