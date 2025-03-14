package com.github.rabbitmq.workqueues;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.rabbitmq.workqueues.Constants.TASK_QUEUE_NAME;

public class WorkQueuesConsumer {

    public static void main(String[] args) throws IOException {
        final Connection connection = RabbitMQConfig.getRabbitMQConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                // Acknowledge the message once it is processed
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        // Start consuming messages from the specified queue without auto-acknowledgement
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
