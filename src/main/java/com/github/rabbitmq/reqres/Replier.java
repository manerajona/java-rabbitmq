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

public class Replier {

    public static void main(String[] args) throws IOException, TimeoutException {

        try (Connection connection = RabbitMQConfig.getRabbitMQConnection();
             Channel channel = connection.createChannel()) {

            // Declare the response exchange as durable for consistency with the requester
            channel.exchangeDeclare(EXCHANGE_RESPONSE, BuiltinExchangeType.FANOUT, true, false, null);

            // Declare the request exchange and bind an anonymous queue to it
            channel.exchangeDeclare(EXCHANGE_REQUEST, BuiltinExchangeType.FANOUT, true, false, null);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_REQUEST, "");

            // Set up the consumer to listen for incoming requests
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                CalculationRequest request = MapperConfig.getMapper().readValue(delivery.getBody(), CalculationRequest.class);
                System.out.println("Request received: " + request);
                CalculationResponse response = getResponse(request);
                byte[] payload = MapperConfig.getMapper().writeValueAsString(response).getBytes();
                channel.basicPublish(EXCHANGE_RESPONSE, "", null, payload);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

            System.out.println("Listening for requests. Press [enter] to exit.");
            // Block the main thread so that it keeps listening for requests
            System.in.read();
        }
    }

    private static CalculationResponse getResponse(CalculationRequest request) {
        final int result;
        final String text;
        if (OperationType.SUBTRACT.equals(request.op())) {
            result = request.int1() - request.int2();
            text = "%d - %d = %d".formatted(request.int1(), request.int2(), result);
        } else {
            result = request.int1() + request.int2();
            text = "%d + %d = %d".formatted(request.int1(), request.int2(), result);
        }
        return new CalculationResponse(result, text);
    }
}
