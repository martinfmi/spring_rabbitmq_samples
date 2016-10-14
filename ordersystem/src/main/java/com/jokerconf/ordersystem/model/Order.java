package com.jokerconf.ordersystem.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Order implements Serializable {

	private static final String DELIMITER = ";";

	private static final String DELIMITER_PART = "=";

	private static final String ATTRIBUTE_TYPE = "type";

	private static final String ATTRIBUTE_PRICE = "price";

	private static final String ATTRIBUTE_QUANTITY = "quantity";

	private String type;

	private int quantity;

	private float price;

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

	public void parseOrder(String rawOrder) {

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
		} else {
			throw new RuntimeException(ATTRIBUTE_TYPE + " is mandatory");
		}

		if (orderAttributes.containsKey(ATTRIBUTE_PRICE)) {
			try {
				price = Float.parseFloat(orderAttributes.get(ATTRIBUTE_PRICE));
			} catch (NumberFormatException ex) {
				throw new RuntimeException(ATTRIBUTE_PRICE + " must be of float type");
			}
		} else {
			throw new RuntimeException(ATTRIBUTE_PRICE + " is mandatory");
		}

		if (orderAttributes.containsKey(ATTRIBUTE_QUANTITY)) {
			try {
				quantity = Integer.parseInt(orderAttributes.get(ATTRIBUTE_QUANTITY));
			} catch (NumberFormatException ex) {
				throw new RuntimeException(ATTRIBUTE_QUANTITY + " must be of integer type");
			}
		}
	}
}
