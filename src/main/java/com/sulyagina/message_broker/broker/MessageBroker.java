package com.sulyagina.message_broker.broker;

import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.Message;

import java.util.Set;


/**
 * Created by anastasia on 27.05.16.
 */
public interface MessageBroker {
    void publish(Message message, String topic);
    boolean subscribe(Listener listener, String topic);
    boolean unSubscribe(Listener listener, String topic);
    Set<Listener> getListenersByTopic(String topic);
}
