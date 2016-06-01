package com.sulyagina.message_broker;

import com.sulyagina.message_broker.broker.Broker;

/**
 * Created by anastasia on 01.06.16.
 */
public class TestBroker extends Broker {
    private EventManager manager;
    public final String QUEUE_EMPTY_EVENT = "QueueEmpty";

    TestBroker(EventManager manager){
        this.manager = manager;
    }

    @Override
    public void close(){
        super.waitClose();
        manager.raiseEvent(QUEUE_EMPTY_EVENT);
    }

}
