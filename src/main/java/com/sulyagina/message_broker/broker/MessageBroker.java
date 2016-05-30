package com.sulyagina.message_broker.broker;

import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.ListenerWrapper;
import com.sulyagina.message_broker.components.Message;
import com.sulyagina.message_broker.components.Topic;

import java.util.Set;

/**
 * Created by anastasia on 27.05.16.
 */
public interface MessageBroker {
    void publish(Message message, Topic topic);
    boolean subscribe(Listener listener, Topic topic);
    boolean unSubscribe(Listener listener, Topic topic);
    public Set<ListenerWrapper> getListenersByTopic(Topic topic);
}
