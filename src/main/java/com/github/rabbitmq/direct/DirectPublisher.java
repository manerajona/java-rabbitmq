package com.github.rabbitmq.direct;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.RabbitMQConstants.*;

public class DirectPublisher {
    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Enable publisher confirms
            channel.confirmSelect();

            // Declare the direct exchange as durable and non-auto-delete
            channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, null);

            // Declare durable queues
            channel.queueDeclare(DIRECT_QUEUE_INFOS_NAME, true, false, false, null);
            channel.queueDeclare(DIRECT_QUEUE_WARNINGS_NAME, true, false, false, null);
            channel.queueDeclare(DIRECT_QUEUE_ERRORS_NAME, true, false, false, null);

            // Bind queues to the exchange using specific routing keys
            channel.queueBind(DIRECT_QUEUE_INFOS_NAME, DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_INFO);
            channel.queueBind(DIRECT_QUEUE_WARNINGS_NAME, DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_WARNING);
            channel.queueBind(DIRECT_QUEUE_ERRORS_NAME, DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_ERROR);

            // Publish messages with corresponding routing keys
            channel.basicPublish(DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_INFO, null, "Message with routing key info.".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_WARNING, null, "Message with routing key warning.".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(DIRECT_EXCHANGE_NAME, DIRECT_ROUTING_KEY_ERROR, null, "Message with routing key error.".getBytes(StandardCharsets.UTF_8));

            // Wait until all published messages are confirmed by the broker
            channel.waitForConfirms();

        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
