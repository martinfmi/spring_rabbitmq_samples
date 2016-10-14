package com.jokerconf.rabbitmq.basic.optional;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RequestReceiver {

	private static final String DEFAULT_QUEUE = "";
	
	private static final String REQUEST_QUEUE = "request_queue";
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RequestReceiver.class);

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

	public void receive() {

		if (channel == null) {
			initialize();
		}

		String message = null;
		try {

			channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(REQUEST_QUEUE, true, consumer);

			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			message = new String(delivery.getBody());
			LOGGER.info("Request received: " + message);

			// do something with the request message ...

			BasicProperties properties = delivery.getProperties();
			if (properties != null) {
				AMQP.BasicProperties amqpProps = new AMQP.BasicProperties();
				amqpProps = amqpProps
						.builder()
						.correlationId(
								String.valueOf(properties.getCorrelationId()))
						.build();
					
				channel.basicPublish(DEFAULT_QUEUE, properties.getReplyTo(), amqpProps,
						"Response message.".getBytes());
			} else {
				LOGGER.warn("Cannot determine response destination for message.");
			}
				
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ShutdownSignalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ConsumerCancelledException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
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
