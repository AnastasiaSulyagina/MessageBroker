package com.sulyagina.message_broker;

import com.sulyagina.message_broker.broker.Broker;
import com.sulyagina.message_broker.broker.MessageBroker;
import com.sulyagina.message_broker.components.Listener;
import com.sulyagina.message_broker.components.Message;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * For testing such library decided not to go with any complicated multithreading things, sleeping for a bit is ok.
 */
public class Tests {
    @Test
    public void testSubscribeReceive() {
        EventManager manager = new EventManager();
        TestBroker broker = new TestBroker(manager);
        Listener listener = new TestListener("TestListener");
        manager.addEvent(broker.QUEUE_EMPTY_EVENT, () -> {
            assertEquals("A message should be received", ((TestListener) listener).getReceived(), 1);
        });
        Message message = new TestMessage("Message");
        String topic = "topic";
        broker.subscribe(listener, topic);
        broker.publish(message, topic);

        broker.close();
    }

    @Test
    public void testDoubleSubscribeReceive() {
        EventManager manager = new EventManager();
        TestBroker broker = new TestBroker(manager);
        Listener listener = new TestListener("TestListener");
        Listener anotherListener = new TestListener("Another TestListener");
        manager.addEvent(broker.QUEUE_EMPTY_EVENT, () -> {
            assertEquals("Message received by first listener", ((TestListener) listener).getReceived(), 1);
            assertEquals("Message received by second listener", ((TestListener) anotherListener).getReceived(), 1);
        });
        Message message = new TestMessage("Message");
        String topic = "topic";
        broker.subscribe(listener, topic);
        broker.subscribe(anotherListener, topic);
        broker.publish(message, topic);
        broker.close();
    }

    @Test
    public void testDoubleSubscribeDoubleReceive() {
        EventManager manager = new EventManager();
        TestBroker broker = new TestBroker(manager);
        Listener listener = new TestListener("TestListener");
        Listener anotherListener = new TestListener("Another TestListener");
        manager.addEvent(broker.QUEUE_EMPTY_EVENT, () -> {
            assertEquals("Message received by first listener", ((TestListener) listener).getReceived(), 2);
            assertEquals("Message received by second listener", ((TestListener) anotherListener).getReceived(), 2);
        });
        String topic = "topic";
        broker.subscribe(listener, topic);
        broker.subscribe(anotherListener, topic);
        broker.publish(new TestMessage("Message"), topic);
        broker.publish(new TestMessage("New message"), topic);

        broker.close();
    }

    @Test
    public void testWrongSubscribeNotReceive() {
        EventManager manager = new EventManager();
        TestBroker broker = new TestBroker(manager);
        Listener listener = new TestListener("TestListener2");
        manager.addEvent(broker.QUEUE_EMPTY_EVENT,
                () -> assertEquals("Message from wrong topic not received", ((TestListener) listener).getReceived(), 0));
        Message message = new TestMessage("Message");
        broker.subscribe(listener, "topic");
        broker.publish(message, "not-listened topic");

        broker.close();
    }

    @Test
    public void testUnsubscribe() {
        EventManager manager = new EventManager();
        TestBroker broker = new TestBroker(manager);
        Listener listener = new TestListener("TestListener3");
        String topic = "topic";
        broker.subscribe(listener, topic);
        broker.publish(new TestMessage("Message"), topic);
        manager.addEvent(broker.QUEUE_EMPTY_EVENT,
                () -> assertEquals("Unsubscribed. Receive nothing", ((TestListener)listener).getReceived(), 1));
        broker.unSubscribe(listener, topic);
        broker.publish(new TestMessage("Message"), topic);
        broker.close();
    }

    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(Tests.class);
        System.out.println("Failed: " + Integer.toString(result.getFailureCount()) +
                ", OK: " + Integer.toString(result.getRunCount() - result.getFailureCount()));
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

    }
}
