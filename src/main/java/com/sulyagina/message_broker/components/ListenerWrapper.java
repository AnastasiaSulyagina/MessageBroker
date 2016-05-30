package com.sulyagina.message_broker.components;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by anastasia on 29.05.16.
 */
@Entity
@Table(name = "listener")
public class ListenerWrapper implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "listener")
    private Listener listener;
    @ManyToMany(mappedBy="topic")
    Set<Topic> topics;

    public ListenerWrapper() {}

    public ListenerWrapper(Listener listener) {
        this.listener = listener;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Listener getListener(){
        return listener;
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }

    public Set<Topic> getTopics(){
        return topics;
    }

    @Override
    public int hashCode() {
        return this.listener.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ListenerWrapper)) {
            return false;
        }
        return this.listener.equals(((ListenerWrapper)obj).getListener());
    }
}
