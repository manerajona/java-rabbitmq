package com.github.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConfig {

    private static final Connection rabbitMQConnection;

    static {
        System.out.println("Initializing RabbitMQ connection");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            rabbitMQConnection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.err.println("Failed to initialize RabbitMQ connection");
            throw new RuntimeException(e);
        }
    }

    private RabbitMQConfig() {
    }

    public static Connection getRabbitMQConnection() {
        return rabbitMQConnection;
    }
}
