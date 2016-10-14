package com.jokerconf.rabbitmq.basic.optional;

import com.jokerconf.rabbitmq.basic.Sender;

public class RequestSenderDemo {

	public static void sendToRequestReplyQueue() {
		Sender sender = new Sender();
		sender.initialize();
		sender.sendRequest("Test message.", "MSG1");
		// String result = sender.waitForResponse("MSG1");
		sender.destroy();
//		return result;
	}

	public static void main(String[] args) {
		sendToRequestReplyQueue();
	}
	
}
