# MessageBroker
MessageBroker library provides message broker implementation. It allows creating custom listeners, subscribing them to
topics and receiving messages from topics they are subscribed to. If application breaks in the middle of broadcast,
when broker starts functioning again, it will continue delivering messages it had in queue before it broke.

## Build
1. run "mvn package" from root
2. get target/message_broker-1.0-SNAPSHOT-jar-with-dependencies.jar file and add it as external library to your project

## Initialization
```JAVA
MessageBroker broker = new Broker();
```

## Creating custom listener
```JAVA
public class CustomListener implements Listener {
    @Override
    public void onReceive(Message message) {
        //put your code here
    }
    ...
}
```

## Creating custom message
```JAVA
public class CustomMessage extends Message {
    public CustomMessage(String text) {
        super(text);
    }
}
```

## Subscribing and publishing
```JAVA
// create your topic
String topic = "topic";

// subscribe
Listener listener = new CustomListener();
broker.subscribe(listener, topic)

// publish
broker.publish(new CustomMessage("Hello, world!"), topic);

// unsubscribe
broker.unsubscribe(listener, topic)
```

## Finishing work
```JAVA
broker.close();
```

## Решения
В итоговом варианте брокера в бд сохраняются только задачи на рассылку, так как хранение тем и листенеров представляется
нецелесообразным при отсутствии сетевого взаимодействия (Если JVM упадет, то упадет и клиент). Рассылка реализована
многопоточно, запись задач в базу тоже в отдельном потоке. Написание логики получения сообщения в листенере
предоставляется клиенту.

## Развитие
1. Масштабируемость:
   1. Размер сообщений: кешировать часто повторяемые сообщения, передавать не сообщение, а его id.
   2. Количество сообщений: отсылать за раз пакет из нескольких сообщений, если не критично получение в real-time
   3. Количество тем: добавить параллельность на уровне отправки сообщений (сейчас одна тема - один поток)
2. Для возможности сетевого соединения можно реализовать сетевое взаимодействие вида "одна нода - сервер, куда приходят
сообщения в очередь, все шлют сообщения на него, он шлет пачки сообщений на другие ноды, ответственные за рассылку на
определенных листенеров"
3. Для сохранения данных при падении сервера необходимо реализовать резервное копирование информации между серверами
