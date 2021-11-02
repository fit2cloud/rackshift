package io.rackshift.config;

import io.rackshift.constants.MqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    @Value("${amqp.uri:'amqp://127.0.0.1'}")
    private String mqURI;

    @Bean
    public CachingConnectionFactory rabbitMQConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(MqConstants.USERNAME);
        connectionFactory.setPassword(MqConstants.PASSWORD);
        connectionFactory.setUri(mqURI);
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
        TopicExchange exchange = new TopicExchange(MqConstants.EXCHANGE_NAME, false, true);
        admin.declareExchange(exchange);
        admin.declareQueue(new Queue(MqConstants.RUN_TASKGRAPH_QUEUE_NAME, false, true, true));
        admin.declareQueue(new Queue(MqConstants.RUN_TASK_QUEUE_NAME, false, true, true));

        admin.declareBinding(new Binding(MqConstants.RUN_TASKGRAPH_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.RUN_TASKGRAPH_ROUTINGKEY + MqConstants.ANY, null));
        admin.declareBinding(new Binding(MqConstants.RUN_TASK_QUEUE_NAME, Binding.DestinationType.QUEUE, MqConstants.EXCHANGE_NAME, MqConstants.RUN_TASK_ROUTINGKEY + MqConstants.ANY, null));
        return new RabbitTemplate(rabbitMQConnectionFactory());
    }

    @Bean
    public MessageConverter mqMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
