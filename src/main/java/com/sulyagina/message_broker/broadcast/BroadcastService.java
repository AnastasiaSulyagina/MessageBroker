package com.sulyagina.message_broker.broadcast;

import com.sulyagina.message_broker.components.BroadcastTask;
import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.ListenerWrapper;
import com.sulyagina.message_broker.broker.MessageBroker;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.components.database.DatabaseUpdateTask;
import com.sulyagina.message_broker.database.DatabaseService;

import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by anastasia on 29.05.16.
 */
public class BroadcastService {
    private ThreadPoolExecutor executor;
    private final MessageBroker broker;
    private final DatabaseService databaseService;

    public BroadcastService(MessageBroker broker, DatabaseService service) {
        //executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        executor = new ThreadPoolExecutor(4, 4, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.broker = broker;
        this.databaseService = service;
    }

    public void addTask(BroadcastTask task) {
        //executor.submit(new BroadcastRunnable(task));
        Set<ListenerWrapper> listeners = broker.getListenersByTopic(task.getTopic());
        for(ListenerWrapper lw : listeners) {
            Listener listener = lw.getListener();
            listener.onReceive(task.getMessage());
        }
        //databaseService.addTask(new DatabaseUpdateTask(DatabaseTask.Entity.BROADCAST, task));
    }

    public class BroadcastRunnable implements Runnable {
        BroadcastTask task;
        Set<ListenerWrapper> listeners;
        public BroadcastRunnable (BroadcastTask task) {
            this.task = task;
            listeners = broker.getListenersByTopic(task.getTopic());
        }
        @Override
        public void run() {
            for(ListenerWrapper lw : listeners) {
                Listener listener = lw.getListener();
                listener.onReceive(task.getMessage());
            }
        }

    }
}
