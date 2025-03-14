package com.github.rabbitmq.pubsub;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import static com.github.rabbitmq.pubsub.Constants.EXCHANGE_NAME;

public class PubSubPublisher {

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = MessageFormat.format("[{0}] [INFO] Hello World!", LocalDateTime.now());
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
