package io.rackshift.utils;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReceiveAndReplyCallback;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.function.Function;

public class MqUtil {
    private static String queueName(String exchange, String routingKey) {
        return exchange + "." + routingKey;
    }

    public static void subscribe(String exchange, String routingKey, Function callback) {
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) SpringUtils.getApplicationContext().getBean("rabbitMQConnectionFactory");
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue(queueName(exchange, routingKey)));
        admin.declareBinding(new Binding(queueName(exchange, routingKey), Binding.DestinationType.QUEUE, exchange, routingKey, null));
        RabbitTemplate r = new RabbitTemplate(connectionFactory);
        r.receiveAndReply(queueName(exchange, routingKey), new ReceiveAndReplyCallback<Object, Object>() {
            @Override
            public Object handle(Object o) {
                callback.apply(o);
                return null;
            }
        });
    }


    public static void publish(String exchange, String routingKey, Message message) {

    }
}
