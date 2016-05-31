package com.sulyagina.message_broker.dao;

import com.sulyagina.message_broker.components.BroadcastTask;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by anastasia on 31.05.16.
 */
@Repository
@Transactional
public class BroadcastDAO extends AbstractDAO {
    public BroadcastDAO(){
        setClazz(BroadcastTask.class);
    }
}
