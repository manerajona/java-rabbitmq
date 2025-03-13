package com.github.rabbitmq.fanout;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.RabbitMQConstants.*;

public class FanoutPublisher {

    public static void main(String[] args) {
        // Using try-with-resources to ensure resources are closed properly
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Declare a fanout exchange, durable and non-auto-delete
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

            // Declare durable queues
            channel.queueDeclare(QUEUE1_NAME, true, false, false, null);
            channel.queueDeclare(QUEUE2_NAME, true, false, false, null);

            // Bind queues to the exchange
            channel.queueBind(QUEUE1_NAME, EXCHANGE_NAME, "");
            channel.queueBind(QUEUE2_NAME, EXCHANGE_NAME, "");

            // Publish messages to the exchange
            channel.basicPublish(EXCHANGE_NAME, "", null, MESSAGE_1.getBytes());
            channel.basicPublish(EXCHANGE_NAME, "", null, MESSAGE_2.getBytes());

            System.out.println("Messages published. Press enter to exit.");
            System.in.read();

            // Clean up: delete queues and exchange
            channel.queueDelete(QUEUE1_NAME);
            channel.queueDelete(QUEUE2_NAME);
            channel.exchangeDelete(EXCHANGE_NAME);

        } catch (IOException | TimeoutException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
