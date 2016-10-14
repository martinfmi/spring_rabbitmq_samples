package com.jokerconf.rabbitmq.basic.optional;

import com.jokerconf.rabbitmq.basic.Sender;

public class FanoutExchangeSenderDemo {

	private static final String FANOUT_EXCHANGE_TYPE = "fanout";

	public static void sendToFanoutExchange(String exchange) {
		Sender sender = new Sender();
		sender.initialize();
		sender.send(exchange, FANOUT_EXCHANGE_TYPE, "Test message.");
		sender.destroy();
	}
	
	public static void main(String[] args) {
		sendToFanoutExchange("pubsub_exchange");
	}
}
