package com.jokerconf.rabbitmq.spring;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RabbitAdminSpringExample {

	public static void main(String[] args) {

		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"configuration.xml");
		RabbitAdmin admin = (RabbitAdmin) context.getBean("amqpAdmin");
		Queue queue = new Queue("test-queue");
		admin.declareQueue(queue);
		context.destroy();
	}
}
