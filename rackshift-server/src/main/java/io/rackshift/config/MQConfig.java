package io.rackshift.config;

import io.rackshift.constants.MqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class MQConfig {
    @Bean
    public CachingConnectionFactory rabbitMQConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(MqConstants.USERNAME);
        connectionFactory.setPassword(MqConstants.PASSWORD);
        connectionFactory.setUri(MqConstants.URI);
        connectionFactory.setVirtualHost(MqConstants.VIRTUALHOST);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(rabbitMQConnectionFactory());
    }

    @Bean
    public AmqpTemplate RabbitTemplate() {
        AmqpAdmin admin = amqpAdmin();
        TopicExchange exchange = new TopicExchange(MqConstants.EXCHANGE_NAME, true, true);
        admin.declareExchange(exchange);
        admin.declareQueue(new Queue(MqConstants.RUN_TASKGRAPH_QUEUE_NAME));
        admin.declareQueue(new Queue(MqConstants.CANCEL_TASKGRAPH_QUEUE_NAME));
        admin.declareQueue(new Queue(MqConstants.RUN_TASK_QUEUE_NAME));
        admin.declareQueue(new Queue(MqConstants.CANCEL_TASK_QUEUE_NAME));

        admin.declareBinding(new Binding(MqConstants.RUN_TASKGRAPH_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.RUN_TASKGRAPH_ROUTINGKEY + MqConstants.ANY, null));
        admin.declareBinding(new Binding(MqConstants.CANCEL_TASKGRAPH_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.CANCEL_TASKGRAPH_ROUTINGKEY + MqConstants.ANY, null));
        admin.declareBinding(new Binding(MqConstants.RUN_TASK_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.RUN_TASK_ROUTINGKEY + MqConstants.ANY, null));
        admin.declareBinding(new Binding(MqConstants.CANCEL_TASK_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.CANCEL_TASK_ROUTINGKEY + MqConstants.ANY, null));
        return new RabbitTemplate(rabbitMQConnectionFactory());
    }

    @Bean
    public MessageConverter mqMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(MqConstants.USERNAME);
        connectionFactory.setPassword(MqConstants.PASSWORD);
        connectionFactory.setUri(MqConstants.URI);
        connectionFactory.setVirtualHost(MqConstants.VIRTUALHOST);

        RabbitTemplate ra = new RabbitTemplate(connectionFactory);
        ra.setExchange("rackshift.exchange.default");
        ra.setDefaultReceiveQueue(MqConstants.RUN_TASKGRAPH_QUEUE_NAME);

        Message m = new Message("hello".getBytes(StandardCharsets.UTF_8));
        ra.send(MqConstants.RUN_TASKGRAPH_ROUTINGKEY + ".12121", m);

//        ra.receiveAndReply(new ReceiveAndReplyCallback<Object, Object>() {
//            @Override
//            public Object handle(Object o) {
//                System.out.println(o);
//                return null;
//            }
//        });
    }

}
