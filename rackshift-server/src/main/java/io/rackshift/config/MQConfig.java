package io.rackshift.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MQConfig {
    @PostConstruct
    public void initRabbitMQ(){

    }

    public static void main(String[] args) {
        CachingConnectionFactory  connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setUri("amqp://127.0.0.1");
        connectionFactory.setVirtualHost("/");
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);

        admin.declareQueue(new Queue("myqueue"));
        AmqpTemplate template = new RabbitTemplate(connectionFactory);
        template.convertAndSend("myqueue", "foo");
        String foo = (String) template.receiveAndConvert("myqueue");
    }
}
