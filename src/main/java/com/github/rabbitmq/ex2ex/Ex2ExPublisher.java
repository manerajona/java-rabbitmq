package com.github.rabbitmq.ex2ex;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.ex2ex.Constants.*;

public class Ex2ExPublisher {

    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Enable publisher confirms for reliable publishing
            channel.confirmSelect();

            // Declare exchanges as durable and non-auto-delete
            channel.exchangeDeclare(EXCHANGE_1, BuiltinExchangeType.DIRECT, true, false, null);
            channel.exchangeDeclare(EXCHANGE_2, BuiltinExchangeType.DIRECT, true, false, null);

            // Declare durable queues
            channel.queueDeclare(QUEUE_1, true, false, false, null);
            channel.queueDeclare(QUEUE_2, true, false, false, null);

            // Bind queues to exchanges using specific routing keys
            channel.queueBind(QUEUE_1, EXCHANGE_1, ROUTING_KEY_1);
            channel.queueBind(QUEUE_2, EXCHANGE_2, ROUTING_KEY_2);

            // Bind exchange2 to exchange1 with a routing key
            channel.exchangeBind(EXCHANGE_2, EXCHANGE_1, ROUTING_KEY_2);

            // Publish message to exchange1 with routing key key1
            channel.basicPublish(EXCHANGE_1, ROUTING_KEY_1, null, "Message key1".getBytes(StandardCharsets.UTF_8));

            // Publish message to exchange1 with routing key key2; it will be forwarded to exchange2 due to the exchange binding
            channel.basicPublish(EXCHANGE_1, ROUTING_KEY_2, null, "Message key2".getBytes(StandardCharsets.UTF_8));

            // Wait until all published messages are confirmed by the broker
            channel.waitForConfirms();
            System.out.println("All messages have been confirmed.");

        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
