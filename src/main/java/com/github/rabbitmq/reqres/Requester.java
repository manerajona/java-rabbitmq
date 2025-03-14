package com.github.rabbitmq.reqres;

import com.github.rabbitmq.MapperConfig;
import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.github.rabbitmq.reqres.Constants.EXCHANGE_REQUEST;
import static com.github.rabbitmq.reqres.Constants.EXCHANGE_RESPONSE;

public class Requester {

    public static void main(String[] args) {

        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Declare the request exchange using BuiltinExchangeType for consistency
            channel.exchangeDeclare(EXCHANGE_REQUEST, BuiltinExchangeType.FANOUT, true, false, null);

            // Declare the response exchange and bind an anonymous queue to it
            channel.exchangeDeclare(EXCHANGE_RESPONSE, BuiltinExchangeType.FANOUT, true, false, null);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_RESPONSE, "");

            // Set up the consumer to listen for responses
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                CalculationResponse response = MapperConfig.getMapper().readValue(delivery.getBody(), CalculationResponse.class);
                System.out.println("Calculation result: " + response.resultText());
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

            // Send requests after the consumer is ready
            sendRequest(channel, EXCHANGE_REQUEST, new CalculationRequest(2, 4, OperationType.ADD));
            sendRequest(channel, EXCHANGE_REQUEST, new CalculationRequest(8, 6, OperationType.SUBTRACT));
            sendRequest(channel, EXCHANGE_REQUEST, new CalculationRequest(20, 7, OperationType.ADD));
            sendRequest(channel, EXCHANGE_REQUEST, new CalculationRequest(50, 8, OperationType.SUBTRACT));

            System.out.println("Listening for responses. Press [enter] to exit.");
            // Block the main thread so that it keeps listening for responses
            System.in.read();

        } catch (IOException | TimeoutException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendRequest(Channel channel, String exchange, CalculationRequest request) throws IOException {
        byte[] payload = MapperConfig.getMapper().writeValueAsString(request).getBytes();
        channel.basicPublish(exchange, "", null, payload);
    }
}
