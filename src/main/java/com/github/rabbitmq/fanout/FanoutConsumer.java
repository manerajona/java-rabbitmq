package com.github.rabbitmq.fanout;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.RabbitMQConstants.QUEUE1_NAME;

public class FanoutConsumer {

    public static void main(String[] args) {
        // Use try-with-resources to automatically close the connection and channel
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Create a consumer that processes incoming messages and acknowledges them
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    System.out.println("Received message: " + message);
                    // Acknowledge the message once it is processed
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            // Start consuming messages from the specified queue without auto-acknowledgement
            channel.basicConsume(QUEUE1_NAME, false, consumer);
            System.out.println("Waiting for messages. Press enter to exit.");
            System.in.read();

        } catch (IOException | TimeoutException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
