package com.github.rabbitmq.pubsub;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.github.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import static com.github.rabbitmq.pubsub.Constants.EXCHANGE_NAME;

public class PubSubConsumer {

	public static void main(String[] argv) throws IOException {
		final Connection connection = RabbitMQConfig.getRabbitMQConnection();
		final Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			System.out.println(" [x] Received '" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	}
}
