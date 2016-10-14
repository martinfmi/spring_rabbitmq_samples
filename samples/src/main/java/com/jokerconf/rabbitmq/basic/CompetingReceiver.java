package com.jokerconf.rabbitmq.basic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class CompetingReceiver {

	private final static String QUEUE_NAME = "event_queue";

	private final static Logger LOGGER = LoggerFactory.getLogger(CompetingReceiver.class);
	
	private Connection connection = null;

	private Channel channel = null;

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
	
	public String receive() {

		if (channel == null) {
			initialize();
		}

		String message = null;
		try {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);
		
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
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
	}
	
}
