package com.sulyagina.message_broker.components;

import java.io.Serializable;

/**
 * Created by anastasia on 27.05.16.
 */
public abstract class Message implements Serializable {
    private String text;

    public Message() {}

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
