package com.sulyagina.message_broker.broker;

import com.sulyagina.message_broker.components.*;
import com.sulyagina.message_broker.broadcast.BroadcastService;
import com.sulyagina.message_broker.components.database.DatabaseDeleteTask;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.components.database.DatabaseUpdateTask;
import com.sulyagina.message_broker.dao.AbstractDAO;
import com.sulyagina.message_broker.database.DatabaseService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anastasia on 27.05.16.
 */
public class Broker implements MessageBroker {
    private Map<Topic, Set<ListenerWrapper>> m;
    private Map<Listener, ListenerWrapper> wrappers;

    private List<Topic> topics;
    private DatabaseService databaseService;
    private BroadcastService broadcastService;

    public Broker() {
        m = new ConcurrentHashMap<>();
        topics = new ArrayList<>();
        databaseService = new DatabaseService();
        broadcastService = new BroadcastService(this, databaseService);
        wrappers = new ConcurrentHashMap<>();
        //loadPreviousState();
        //loadTasks();
    }
    private void loadPreviousState() {
        topics = ((AbstractDAO<Topic>)databaseService.getDAO(DatabaseTask.Entity.TOPIC)).findAll();
        for (Topic t: topics) {
            Set<ListenerWrapper> topicListeners = t.getListeners();
            m.put(t, topicListeners);
        }
    }

    private void loadTasks() {
        List<BroadcastTask> tasks = ((AbstractDAO<BroadcastTask>)databaseService.
                getDAO(DatabaseTask.Entity.BROADCAST)).findAll();
        for(BroadcastTask t: tasks) {
            broadcastService.addTask(t);
        }
    }

    public void publish(Message message, Topic topic) {
        if(topics.contains(topic)) {
            broadcastService.addTask(new BroadcastTask(message, topic));
        } else {
            System.err.println("No topic '" + topic.getName() + "' exists, nobody listens.");
        }
    }

    public boolean subscribe(Listener listener, Topic topic) {
        if (!topics.contains(topic)) {
            topics.add(topic);
            m.put(topic, new HashSet<>());
        }
        ListenerWrapper wrap;
        if (wrappers.containsKey(listener)) {
            wrap = wrappers.get(listener);
        } else {
            wrap = new ListenerWrapper(listener);
            wrappers.put(listener, wrap);
        }
        m.get(topic).add(wrap);
        //databaseService.addTask(new DatabaseUpdateTask(DatabaseTask.Entity.TOPIC, topic));
        return true;
    }

    public boolean unSubscribe(Listener listener, Topic topic) {
        if (!wrappers.containsKey(listener)) {
            System.out.println("Can not unsubscribe. Listener not subscribed.");
            return false;
        }
        ListenerWrapper listenerWrapper = wrappers.get(listener);
        m.get(topic).remove(listenerWrapper);
        //databaseService.addTask(new DatabaseUpdateTask(DatabaseTask.Entity.TOPIC, topic));
        //if(listenerWrapper.getTopics().size() == 0) {
        //    databaseService.addTask(new DatabaseDeleteTask(DatabaseTask.Entity.LISTENER, listenerWrapper));
        //}
        return true;
    }
    public Set<ListenerWrapper> getListenersByTopic(Topic topic) {
        return new HashSet<>(m.get(topic));
    }
}
