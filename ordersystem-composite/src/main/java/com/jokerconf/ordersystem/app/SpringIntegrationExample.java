package com.jokerconf.ordersystem.app;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.support.GenericMessage;

import com.jokerconf.ordersystem.model.Order;

public class SpringIntegrationExample {

	public static void main(String[] args) throws IOException {

		AbstractApplicationContext context = new ClassPathXmlApplicationContext("configuration-int.xml");
		DirectChannel channel = (DirectChannel) context.getBean("test-channel");
		 
		List<Order> orders = readOrders();
		for (Order order : orders) {
			channel.send(new GenericMessage<Order>(order));
		}
		context.destroy();
	}
	
	private static List<Order> readOrders() throws IOException {

		LineNumberReader lineReader = null;
		try {
			List<Order> orders = new LinkedList<Order>();
			FileReader orderReader = new FileReader(SpringIntegrationExample.class.getResource("/orders.txt").getFile());
			lineReader = new LineNumberReader(orderReader);

			String line = lineReader.readLine();
			while (line != null) {
				Order order = new Order();
				order.parseOrder(line);
				orders.add(order);
				line = lineReader.readLine();
			}

			return orders;
		} finally {
			if (lineReader != null) {
				lineReader.close();
			}
		}
	}
}
