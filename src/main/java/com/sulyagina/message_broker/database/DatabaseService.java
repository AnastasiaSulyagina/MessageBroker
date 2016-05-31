package com.sulyagina.message_broker.database;

import com.sulyagina.message_broker.components.*;
import com.sulyagina.message_broker.components.database.DatabaseTask;
import com.sulyagina.message_broker.dao.BroadcastDAO;
import com.sulyagina.message_broker.dao.AbstractDAO;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by anastasia on 30.05.16.
 */
public class DatabaseService {
    private ThreadPoolExecutor executor;
    private AbstractDAO<BroadcastTask> broadcastDAO;

    public DatabaseService(ApplicationContext context) {
        executor = new ThreadPoolExecutor(1, 1, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        BeanFactory factory = context;
        broadcastDAO = factory.getBean(BroadcastDAO.class);
    }

    public AbstractDAO getDAO(DatabaseTask.Entity entityType) {
        switch (entityType) {
            case BROADCAST:
                return broadcastDAO;
            default:
                return null;
        }
    }

    public void addTask(DatabaseTask task) {
        executor.submit(new DatabaseRunnable(task));
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
