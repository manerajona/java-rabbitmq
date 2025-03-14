package com.github.rabbitmq.topic;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.topic.Constants.TOPIC_QUEUE_IMG_1_NAME;

public class TopicConsumer {

    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Create a consumer that processes incoming messages and acknowledges them
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("Received message: " + message);
                    // Acknowledge the message once it is processed
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            // Start consuming messages from the specified queue without auto-acknowledgement
            channel.basicConsume(TOPIC_QUEUE_IMG_1_NAME, false, consumer);
            System.out.println("Waiting for messages. Press enter to exit.");
            System.in.read();

        } catch (IOException | TimeoutException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
