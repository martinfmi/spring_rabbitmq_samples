package com.jokerconf.rabbitmq.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RabbitTemplateSpringExample {

	public static void main(String[] args) {

		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"configuration.xml");
		RabbitTemplate template = (RabbitTemplate) context.getBean("amqpTemplate");
		template.convertAndSend("Sample Spring test message.");
		context.destroy();
	}
}
