package com.github.rabbitmq.ex2ex;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.ex2ex.Constants.QUEUE_1;

public class Ex2ExConsumer {

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
            channel.basicConsume(QUEUE_1, false, consumer);
            System.out.println("Waiting for messages. Press enter to exit.");
            System.in.read();

        } catch (IOException | TimeoutException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
