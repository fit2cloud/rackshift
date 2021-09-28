package io.rackshift.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class MqUtil {
    private static String queueName(String exchange, String routingKey) {
        return exchange + "." + routingKey;
    }

    public static void subscribe(String exchange, String routingKey, Function<Object, Object> callback) {
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) SpringUtils.getApplicationContext().getBean("rabbitMQConnectionFactory");
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue(queueName(exchange, routingKey)));
        admin.declareBinding(new Binding(queueName(exchange, routingKey), Binding.DestinationType.QUEUE, exchange, routingKey, null));
        RabbitTemplate r = new RabbitTemplate(connectionFactory);
        r.receiveAndReply(queueName(exchange, routingKey), o -> callback.apply(o));
    }

    public static void publish(String exchange, String routingKey, Message message) {
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) SpringUtils.getApplicationContext().getBean("rabbitMQConnectionFactory");
        RabbitTemplate r = new RabbitTemplate(connectionFactory);
        r.setExchange(exchange);
        r.send(routingKey, message);
    }

    public static Message request(String exchange, String routingKey) {
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) SpringUtils.getApplicationContext().getBean("rabbitMQConnectionFactory");
        RabbitTemplate r = new RabbitTemplate(connectionFactory);
        r.setExchange(exchange);
        return r.sendAndReceive(routingKey, new Message("hello".getBytes(StandardCharsets.UTF_8)));
    }
}
