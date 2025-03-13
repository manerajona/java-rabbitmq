package com.github.rabbitmq.topic;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.RabbitMQConstants.*;

public class TopicPublisher {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            //Enable channel level publisher confirms (to be able to get confirmations from RabbitMQ broker)
            channel.confirmSelect();

            // Declare the topic exchange as durable and non-auto-delete
            channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true, false, null);

            // Declare durable queues
            channel.queueDeclare(TOPIC_QUEUE_IMG_1_NAME, true, false, false, null);
            channel.queueDeclare(TOPIC_QUEUE_IMG_2_NAME, true, false, false, null);
            channel.queueDeclare(TOPIC_QUEUE_IMG_3_NAME, true, false, false, null);

            // Bind queues to the exchange using topic patterns
            channel.queueBind(TOPIC_QUEUE_IMG_1_NAME, TOPIC_EXCHANGE_NAME, TOPIC_BINDING_KEY_IMG_1);
            channel.queueBind(TOPIC_QUEUE_IMG_2_NAME, TOPIC_EXCHANGE_NAME, TOPIC_BINDING_KEY_IMG_2);
            channel.queueBind(TOPIC_QUEUE_IMG_3_NAME, TOPIC_EXCHANGE_NAME, TOPIC_BINDING_KEY_IMG_3);

            // Publish messages with corresponding routing keys
            channel.basicPublish(TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY_1, null, "Routing key is convert.image.bmp".getBytes());
            channel.basicPublish(TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY_2, null, "Routing key is convert.bitmap.image".getBytes());
            channel.basicPublish(TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY_3, null, "Routing key is image.bitmap.32bit".getBytes());

            //Wait until all published messages are confirmed
            channel.waitForConfirms();
        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
