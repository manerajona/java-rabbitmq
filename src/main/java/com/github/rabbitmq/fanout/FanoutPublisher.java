package com.github.rabbitmq.fanout;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.fanout.Constants.*;

public class FanoutPublisher {

    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Enable publisher confirms
            channel.confirmSelect();

            // Declare a fanout exchange, durable and non-auto-delete
            channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

            // Declare durable queues
            channel.queueDeclare(FANOUT_QUEUE_1_NAME, true, false, false, null);
            channel.queueDeclare(FANOUT_QUEUE_2_NAME, true, false, false, null);

            // Bind queues to the exchange
            channel.queueBind(FANOUT_QUEUE_1_NAME, FANOUT_EXCHANGE_NAME, "");
            channel.queueBind(FANOUT_QUEUE_2_NAME, FANOUT_EXCHANGE_NAME, "");

            // Publish messages to the exchange
            channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, "Message 1".getBytes());
            channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, "Message 2".getBytes());

            // Wait until all published messages are confirmed by the broker
            channel.waitForConfirms();

        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
