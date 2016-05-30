package com.sulyagina.message_broker.components;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by anastasia on 28.05.16.
 */
@Entity
@Table(name = "topic")
public class Topic implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name="topic_listener",
            joinColumns={@JoinColumn(name="topic_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="listener_id", referencedColumnName="id")})
    private Set<ListenerWrapper> listeners;

    public Topic() {}

    public Topic(String topicName) {
        this.name = topicName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ListenerWrapper> getListeners() {
        return listeners;
    }
}
