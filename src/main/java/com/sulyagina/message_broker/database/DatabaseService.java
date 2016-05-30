package com.sulyagina.message_broker.database;

import com.sulyagina.message_broker.components.*;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.dao.CommonDAO;
import com.sulyagina.message_broker.dao.AbstractDAO;
import com.sulyagina.message_broker.dao.ListenerDAO;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by anastasia on 30.05.16.
 */
public class DatabaseService {
    private ThreadPoolExecutor executor;
    private AbstractDAO<ListenerWrapper> listenerDAO;
    private AbstractDAO<Topic> topicDAO;
    private AbstractDAO<BroadcastTask> broadcastDAO;

    public DatabaseService() {
        //executor = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
        executor = new ThreadPoolExecutor(1, 1, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        listenerDAO = new ListenerDAO<>();
        topicDAO = new CommonDAO<>();
        broadcastDAO = new CommonDAO<>();
    }

    public AbstractDAO getDAO(DatabaseTask.Entity entityType) {
        switch (entityType) {
            case LISTENER:
                return listenerDAO;
            case BROADCAST:
                return broadcastDAO;
            case TOPIC:
                return topicDAO;
            default:
                return new CommonDAO<>();
        }
    }

    public void addTask(DatabaseTask task) {
        //executor.submit(new DatabaseRunnable(task));
        AbstractDAO dao = getDAO(task.getEntityType());
        task.writeTo(dao);
    }

    public class DatabaseRunnable implements Runnable {
        DatabaseTask task;
        public DatabaseRunnable (DatabaseTask task) {
            this.task = task;
        }
        @Override
        public void run() {
            AbstractDAO dao = getDAO(task.getEntityType());
            task.writeTo(dao);
        }
    }
}
