package io.rackshift.utils;

import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class MqUtil {
    private static boolean durable = true;
    private static boolean exclusive = false;

    private static String queueName(String exchange, String routingKey) {
        return exchange + "." + routingKey;
    }

    private static CachingConnectionFactory getCachingConnectionFactory() {
        return (CachingConnectionFactory) SpringUtils.getApplicationContext().getBean("rabbitMQConnectionFactory");
    }

    public static void subscribe(String exchange, String routingKey, Function<Object, Object> callback) {
        CachingConnectionFactory connectionFactory = getCachingConnectionFactory();
        Connection connection = null;
        try {
            // 1.connection & channel
            connection = connectionFactory.createConnection();
            final Channel channel = connection.createChannel(false);

            // 2.declare queue
            channel.queueDeclare(queueName(exchange, routingKey), durable, exclusive, true, null);

            // 3.Receive only one message at a time (task)
            channel.basicQos(1);
            //4.Get consumer instances
            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties prop = new AMQP.BasicProperties().builder().correlationId(properties.getCorrelationId())
                            .build();
                    String resp = "";
                    try {
                        String msg = new String(body, "UTF-8");
                        resp = (String) callback.apply(msg);
                        LogUtil.info("*** will response to rpc client :" + resp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        channel.basicPublish("", properties.getReplyTo(), prop, resp.getBytes());
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            // 5.Consumption messages (processing tasks)
            channel.basicConsume(queueName(exchange, routingKey), false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String request(String exchange, String routingKey, String msg) throws IOException, InterruptedException {
        CachingConnectionFactory connectionFactory = getCachingConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        String queueName = channel.queueDeclare().getQueue();
        final String uuid = UUID.randomUUID().toString();
        //Subsequently, the server relies on"replyTo"To specify to which queue the return information will be written
        //Subsequently, the server identifies based on the Association"correlationId"To specify which request the response was returned
        AMQP.BasicProperties prop = new AMQP.BasicProperties().builder().replyTo(queueName).correlationId(uuid).build();

        channel.basicPublish("", queueName(exchange, routingKey), prop, msg.getBytes());
        final BlockingQueue<String> blockQueue = new ArrayBlockingQueue<String>(1);
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {

                if (properties.getCorrelationId().equals(uuid)) {
                    String msg = new String(body, "UTF-8");

                    blockQueue.offer(msg);
                    LogUtil.info("**** rpc client reciver response :[" + msg + "]");
                    channel.queueDelete(queueName);
                    try {
                        channel.close();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        String r = "no";
        try {
            r = blockQueue.poll(1, TimeUnit.MINUTES);
        } catch (Exception e) {

        } finally {
            channel.queueDelete(queueName);
            try {
                channel.close();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        return r;
    }

    public static void delQueue(String exchangeName, String s) {
        CachingConnectionFactory connectionFactory = getCachingConnectionFactory();
        Connection c = connectionFactory.createConnection();
        Channel channel = c.createChannel(false);
        try {
            channel.queueDelete(queueName(exchangeName, s));
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
