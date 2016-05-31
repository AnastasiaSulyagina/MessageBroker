package com.sulyagina.message_broker.components;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by anastasia on 29.05.16.
 */
@Entity
@Table(name = "broadcast")
public class BroadcastTask implements Serializable{
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name="message")
    private Message message;
    @Column(name="topic")
    private String topic;

    public BroadcastTask(Message message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic(){
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Message getMessage(){
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
