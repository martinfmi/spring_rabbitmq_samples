package com.jokerconf.ordersystem.splitter;

import java.util.List;

import org.springframework.integration.annotation.Splitter;
import org.springframework.stereotype.Component;

import com.jokerconf.ordersystem.model.Order;

@Component("orderSplitter")
public class OrderSplitter {
	
	@Splitter
	public List<Order> split(Order order) {
		return order.getOrders();
	}
}
