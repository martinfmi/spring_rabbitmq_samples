package com.jokerconf.rabbitmq.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ListenerSpringExample {

	public void receiveMessage(String message) {
		System.out.println("Message received: " + message);
	}
	
	public static void main(String[] args) {
		AbstractApplicationContext context =
		        new ClassPathXmlApplicationContext("configuration.xml");
	}
}
