package com.jokerconf.rabbitmq.basic.optional;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class PublishSubscribeReceiver {

	private final static String EXCHANGE_NAME = "pubsub_exchange";

	private final static Logger LOGGER = LoggerFactory.getLogger(PublishSubscribeReceiver.class);

	private Channel channel = null;

	private Connection connection = null;

	public void initialize() {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public String receive(String queue) {

		if (channel == null) {
			initialize();
		}

		String message = null;
		try {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			channel.queueDeclare(queue, false, false, false, null);
			channel.queueBind(queue, EXCHANGE_NAME, "");

			QueueingConsumer consumer = new QueueingConsumer(channel);
			
			channel.basicConsume(queue, true, consumer);
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			message = new String(delivery.getBody());
			LOGGER.info("Message received: " + message);
			return message;

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ShutdownSignalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ConsumerCancelledException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return message;
	}

	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
		}
	}

}
