package com.sulyagina.message_broker;

import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.Message;

/**
 * Created by anastasia on 30.05.16.
 */
public class TestListener implements Listener {
    private volatile int received;
    private String name;

    public TestListener(String s) {
        this.name = s;
    }
    @Override
    public void onReceive(Message message) {
        System.out.println(message.getText() + " received! (" + this.name + ")");
        received += 1;
    }
    public int getReceived(){
        return received;
    }
}
