package com.sulyagina.message_broker;

import com.sulyagina.message_broker.broker.Broker;
import com.sulyagina.message_broker.broker.MessageBroker;
import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.Message;
import com.sulyagina.message_broker.components.Topic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by anastasia on 29.05.16.
 */
public class Tests {
    @Test
    public void testSubscribeReceive() {
        MessageBroker broker = new Broker();
        Listener listener = new TestListener();
        Message message = new TestMessage("Message");
        Topic topic = new Topic("topic");
        broker.subscribe(listener, topic);
        broker.publish(message, topic);
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            System.err.println("fail1");
        }
        assertEquals("A message should be received", ((TestListener) listener).getReceived(), 1);
    }

    @Test
    public void testSubscribeNotReceive() {
        MessageBroker broker = new Broker();
        Listener listener = new TestListener();
        Message message = new TestMessage("Message");
        broker.subscribe(listener, new Topic("topic"));
        broker.publish(message, new Topic("another topic"));
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            System.err.println("fail2");
        }
        assertEquals("Message from wrong topic not to be received", ((TestListener) listener).getReceived(), 0);
    }

    @Test
    public void testUnsubscribe() {
        MessageBroker broker = new Broker();
        Listener listener = new TestListener();
        Topic topic = new Topic("topic");
        broker.subscribe(listener, topic);
        broker.publish(new TestMessage("Message"), topic);
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            System.err.println("fail3");
        }
        assertEquals("Message should be received", ((TestListener)listener).getReceived(), 1);
        broker.unSubscribe(listener, topic);
        broker.publish(new TestMessage("New message"), topic);
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            System.err.println("fail3");
        }
        assertEquals("Unsubscribed. receive nothing", ((TestListener)listener).getReceived(), 1);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Tests.class);
        System.out.println(result.getFailureCount());
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

    }
}
