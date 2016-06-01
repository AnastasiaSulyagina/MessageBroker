package com.sulyagina.message_broker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anastasia on 01.06.16.
 */
public class EventManager {
    private Map<String, List<Runnable>> m = new ConcurrentHashMap<>();
    public EventManager(){

    }

    public void addEvent(String event, Runnable handler){
        if (!m.containsKey(event)) {
            m.put(event, new ArrayList<>());
        }
        m.get(event).add(handler);
    }
    public void clear(String event){
        if (m.containsKey(event)) {
            m.get(event).clear();
        }
    }

    public void raiseEvent(String event) {
        if (m.containsKey(event)) {
            for(Runnable handler: m.get(event)) {
                handler.run();
            }
        }
    }
}
