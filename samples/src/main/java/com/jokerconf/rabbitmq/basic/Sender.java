package com.jokerconf.rabbitmq.basic;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Sender {

	private final static String QUEUE_NAME = "event_queue";

	private final static Logger LOGGER = LoggerFactory.getLogger(Sender.class);

	private static final String DEFAULT_EXCHANGE = "";

	private static final String REQUEST_QUEUE = "request_queue";
	
	private static final String RESPONSE_QUEUE = "response_queue";
	
	private static final String SEMINAR_QUEUE = "seminar_queue";
	
	private static final String HACKATON_QUEUE = "hackaton_queue";
	
	private static final String TOPIC_EXCHANGE = "topic_exchange";
	
	private Channel channel;

	private Connection connection;

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

	public void send(String message) {
		try {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_NAME, null,
					message.getBytes());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void send(String exchange, String type, String message) {
		try {
			channel.exchangeDeclare(exchange, type);

			channel.basicPublish(exchange, "", null, message.getBytes());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void sendRequest(String message, String correlationId) {
		try {
			channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);
			channel.queueDeclare(RESPONSE_QUEUE, false, false, false, null);
			AMQP.BasicProperties amqpProps = new AMQP.BasicProperties();
			amqpProps = amqpProps.builder()
					.correlationId(String.valueOf(correlationId))
					.replyTo(RESPONSE_QUEUE).build();
			channel.basicPublish(DEFAULT_EXCHANGE, REQUEST_QUEUE, amqpProps,
					message.getBytes());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void sendEvent(String exchange, String message, String messageKey) {
		try {
			channel.exchangeDeclare(TOPIC_EXCHANGE, "topic");
			channel.queueDeclare(SEMINAR_QUEUE, false, false, false, null);
			channel.queueDeclare(HACKATON_QUEUE, false, false, false, null);
			channel.queueBind(SEMINAR_QUEUE, TOPIC_EXCHANGE, "seminar.#");
			channel.queueBind(HACKATON_QUEUE, TOPIC_EXCHANGE, "hackaton.#");
			channel.basicPublish(TOPIC_EXCHANGE, messageKey, null,
					message.getBytes());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public String waitForResponse(
			final String correlationId) {

		QueueingConsumer consumer = new QueueingConsumer(channel);
		String result = null;

		try {
			channel.basicConsume(RESPONSE_QUEUE, true, consumer);
			QueueingConsumer.Delivery delivery = consumer.nextDelivery(3000);
			String message = new String(delivery.getBody());
			if (delivery.getProperties() != null) {
				String msgCorrelationId = delivery.getProperties()
						.getCorrelationId();
				if (!correlationId.equals(msgCorrelationId)) {
					LOGGER.warn("Received response of another request.");
				} else {
					result = message;
				}
			}

			LOGGER.info("Message received: " + message);

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ShutdownSignalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ConsumerCancelledException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return result;
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
