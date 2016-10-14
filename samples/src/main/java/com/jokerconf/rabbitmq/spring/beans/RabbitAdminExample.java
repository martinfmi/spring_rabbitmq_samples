package com.jokerconf.rabbitmq.spring.beans;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

public class RabbitAdminExample {

	public static void main(String[] args) {
		CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
		RabbitAdmin admin = new RabbitAdmin(factory);
		Queue queue = new Queue("test-queue");
		admin.declareQueue(queue);
		TopicExchange exchange = new TopicExchange("sample-topic-exchange");
		admin.declareExchange(exchange);
		admin.declareBinding(BindingBuilder.bind(queue).to(exchange)
				.with("sample-key"));
		
		factory.destroy();
	}

}
