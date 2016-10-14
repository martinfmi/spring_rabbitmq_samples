package com.jokerconf.rabbitmq.spring.integration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringIntegrationExample {

	public static void main(String[] args) {
		
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"configuration-int.xml");
		RabbitTemplate template = context.getBean(RabbitTemplate.class);
		template.convertAndSend("test message ...");
	}
}
