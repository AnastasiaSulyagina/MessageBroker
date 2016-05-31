package com.sulyagina.message_broker.broker;

import com.sulyagina.message_broker.components.*;
import com.sulyagina.message_broker.broadcast.BroadcastService;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.dao.AbstractDAO;
import com.sulyagina.message_broker.database.DatabaseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anastasia on 27.05.16.
 */
public class Broker implements MessageBroker {
    private ApplicationContext context;
    private Map<String, Set<Listener>> m;
    private List<String> topics;
    private DatabaseService databaseService;
    private BroadcastService broadcastService;

    public Broker() {
        context = new ClassPathXmlApplicationContext("classpath:spring.config.xml");
        databaseService = new DatabaseService(context);
        broadcastService = new BroadcastService(this, databaseService);
        m = new ConcurrentHashMap<>();
        topics = new ArrayList<>();
        loadTasks();
    }

    private void loadTasks() {
        List<BroadcastTask> tasks = ((AbstractDAO<BroadcastTask>)databaseService.
                getDAO(DatabaseTask.Entity.BROADCAST)).findAll();
        for(BroadcastTask t: tasks) {
            topics.add(t.getTopic());
            m.put(t.getTopic(), new HashSet<>());
            broadcastService.addTask(t);
        }
    }

    public void publish(Message message, String topic) {
        if(topics.contains(topic)) {
            broadcastService.addTask(new BroadcastTask(message, topic));
        } else {
            System.out.println("No topic '" + topic + "' exists, nobody listens.");
        }
    }

    public boolean subscribe(Listener listener, String topic) {
        if(!topics.contains(topic)) {
            topics.add(topic);
            m.put(topic, new HashSet<>());
        }
        m.get(topic).add(listener);
        return true;
    }

    public boolean unSubscribe(Listener listener, String topic) {
        if (!topics.contains(topic) || !m.get(topic).contains(listener)) {
            System.out.println("Can not unsubscribe. Listener not subscribed.");
            return false;
        }
        m.get(topic).remove(listener);
        return true;
    }

    @Override
    public Set<Listener> getListenersByTopic(String topic) {
        if (m.containsKey(topic)) {
            return m.get(topic);
        } else {
            return new HashSet<>();
        }
    }
}
