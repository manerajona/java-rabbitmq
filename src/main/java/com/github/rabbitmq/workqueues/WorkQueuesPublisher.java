package com.github.rabbitmq.workqueues;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class WorkQueuesPublisher {

    static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Enable publisher confirms
            channel.confirmSelect();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, "First message.".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, "Second message..".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, "Second message..".getBytes(StandardCharsets.UTF_8));

            // Wait until all published messages are confirmed by the broker
            channel.waitForConfirms();

        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
