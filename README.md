# MessageBroker
MassageBroker library provides message broker implementation that allows.

## Initialization
```
MessageBroker broker = new Broker();
```

## Creating custom listener
```
public class CustomListener implements Listener {
    @Override
    public void onReceive(Message message) {
        //put your code here
    }
    ...
}
```

## Creating custom message
```
public class CustomMessage extends Message {
    public CustomMessage(String text) {
        super(text);
    }
}
```

## Subscribing and publishing
```
// create your topic
Topic topic = new Topic("topic");

// subscribe
Listener listener = new CustomListener();
broker.subscribe(listener, topic)

// publish
broker.publish(new CustomMessage("Hello, world!"), topic);

// unsubscribe
broker.unsubscribe(listener, topic)
```
