package com.github.rabbitmq.headers;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.headers.Constants.*;

public class HeadersPublisher {

    public static void main(String[] args) {
        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            //Enable channel level publisher confirms (to be able to get confirmations from RabbitMQ broker)
            channel.confirmSelect();

            // Declare the headers exchange (durable, non-auto-delete)
            channel.exchangeDeclare(HEADERS_EXCHANGE_NAME, BuiltinExchangeType.HEADERS, true, false, null);

            // Declare durable queues
            channel.queueDeclare(HEADERS_QUEUE_JPEG_NAME, true, false, false, null);
            channel.queueDeclare(HEADERS_QUEUE_BPM_NAME, true, false, false, null);

            // Bind queue with headers binding (format: jpeg)
            Map<String, Object> jpegArgsQueue = new HashMap<>();
            jpegArgsQueue.put(HEADER_X_MATCH, "all");
            jpegArgsQueue.put(HEADER_TYPE, "Raster");
            jpegArgsQueue.put(HEADER_FORMAT, "jpeg");

            channel.queueBind(HEADERS_QUEUE_JPEG_NAME, HEADERS_EXCHANGE_NAME, "", jpegArgsQueue);

            // Bind queue with headers binding (format: bpm)
            Map<String, Object> bpmArgsQueue = new HashMap<>();
            bpmArgsQueue.put(HEADER_X_MATCH, "all");
            bpmArgsQueue.put(HEADER_TYPE, "Raster");
            bpmArgsQueue.put(HEADER_FORMAT, "bpm");

            channel.queueBind(HEADERS_QUEUE_BPM_NAME, HEADERS_EXCHANGE_NAME, "", bpmArgsQueue);

            // Publish jpeg message
            Map<String, Object> jpegMessageHeaders = new HashMap<>();
            jpegMessageHeaders.put(HEADER_TYPE, "Raster");
            jpegMessageHeaders.put(HEADER_FORMAT, "jpeg");
            AMQP.BasicProperties jpegMessageProperties = new AMQP.BasicProperties.Builder().headers(jpegMessageHeaders).build();

            channel.basicPublish(HEADERS_EXCHANGE_NAME, "", jpegMessageProperties, "message.jpeg".getBytes());

            // Publish bpm message
            Map<String, Object> bpmMessageHeaders = new HashMap<>();
            bpmMessageHeaders.put(HEADER_TYPE, "Raster");
            bpmMessageHeaders.put(HEADER_FORMAT, "bpm");
            AMQP.BasicProperties bpmMessageProperties = new AMQP.BasicProperties.Builder().headers(bpmMessageHeaders).build();

            channel.basicPublish(HEADERS_EXCHANGE_NAME, "", bpmMessageProperties, "message.bpm".getBytes());

            //Wait until all published messages are confirmed
            channel.waitForConfirms();

        } catch (IOException | TimeoutException | InterruptedException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
