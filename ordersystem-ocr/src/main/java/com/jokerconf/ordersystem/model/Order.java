package com.jokerconf.ordersystem.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {

	private static final String DELIMITER = ";";

	private static final String DELIMITER_PART = "=";

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String ATTRIBUTE_PRICE = "price";

	private static final String ATTRIBUTE_QUANTITY = "quantity";

	private static final Object ATTRIBUTE_URL = "url";

	private String ATTRIBUTE_ORDERS = "orders";

	private List<Order> orders = new LinkedList<Order>();

	private String type;

	private int quantity;

	private float price;

	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void parseOrder(String rawOrder) {

		String rawSuborders = null;
		int startOrdersIndex = rawOrder.indexOf("(");
		int endOrdersIndex = rawOrder.indexOf(")");
		if (startOrdersIndex >= 0 && endOrdersIndex >= 0) {
			rawSuborders = rawOrder.substring(startOrdersIndex, endOrdersIndex + 1);
		}
		;
		rawOrder = rawOrder.replaceFirst("\\(.*?\\)", "#");

		String[] parts = rawOrder.split(DELIMITER);
		Map<String, String> orderAttributes = new HashMap<String, String>();

		for (String part : parts) {
			String[] partItems = part.split(DELIMITER_PART);
			if (partItems.length != 2) {
				throw new RuntimeException("Order attribute must be in the format [property] = [value]");
			}

			orderAttributes.put(partItems[0], partItems[1]);
		}

		if (orderAttributes.containsKey(ATTRIBUTE_TYPE)) {
			type = orderAttributes.get(ATTRIBUTE_TYPE);
		}

		if (orderAttributes.containsKey(ATTRIBUTE_PRICE)) {
			try {
				price = Float.parseFloat(orderAttributes.get(ATTRIBUTE_PRICE));
			} catch (NumberFormatException ex) {
				throw new RuntimeException(ATTRIBUTE_PRICE + " must be of float type");
			}
		}

		if (orderAttributes.containsKey(ATTRIBUTE_QUANTITY)) {
			try {
				quantity = Integer.parseInt(orderAttributes.get(ATTRIBUTE_QUANTITY));
			} catch (NumberFormatException ex) {
				throw new RuntimeException(ATTRIBUTE_QUANTITY + " must be of integer type");
			}
		}

		handleExtraAttributes(orderAttributes, rawSuborders);
	}

	public void handleExtraAttributes(Map<String, String> attributes, String rawSuborders) {
		if (attributes.containsKey(ATTRIBUTE_ORDERS)) {
			String rawOrders = rawSuborders;
			rawOrders = rawOrders.replace("(", "");
			rawOrders = rawOrders.replace(")", "");
			String[] suborders = rawOrders.split("\\|");
			for (String rawSuborder : suborders) {
				Order order = new Order();
				order.parseOrder(rawSuborder);
				orders.add(order);
			}
		}

		if (attributes.containsKey(ATTRIBUTE_URL)) {
			url = attributes.get(ATTRIBUTE_URL);
		}
	}

}
