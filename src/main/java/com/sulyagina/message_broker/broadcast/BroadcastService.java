package com.sulyagina.message_broker.broadcast;

import com.sulyagina.message_broker.components.BroadcastTask;
import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.broker.MessageBroker;
import com.sulyagina.message_broker.components.database.DatabaseDeleteTask;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.components.database.DatabaseUpdateTask;
import com.sulyagina.message_broker.database.DatabaseService;

import java.util.Collections;
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
        executor = new ThreadPoolExecutor(4, 4, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.broker = broker;
        this.databaseService = service;
    }

    public void addTask(BroadcastTask task) {
        databaseService.addTask(new DatabaseUpdateTask(DatabaseTask.Entity.BROADCAST, task));
        executor.submit(new BroadcastRunnable(task));
    }

    public class BroadcastRunnable implements Runnable {
        BroadcastTask task;
        Set<Listener> listeners;
        public BroadcastRunnable (BroadcastTask task) {
            this.task = task;
            listeners = broker.getListenersByTopic(task.getTopic());
        }
        @Override
        public void run() {
            for(Listener listener : listeners) {
                listener.onReceive(task.getMessage());
            }
            databaseService.addTask(new DatabaseDeleteTask(DatabaseTask.Entity.BROADCAST, task));
        }

    }
}
