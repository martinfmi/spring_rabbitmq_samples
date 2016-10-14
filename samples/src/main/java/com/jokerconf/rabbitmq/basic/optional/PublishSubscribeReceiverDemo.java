package com.jokerconf.rabbitmq.basic.optional;

public class PublishSubscribeReceiverDemo {

	public static void main(String[] args) throws InterruptedException {
		final PublishSubscribeReceiver receiver1 = new PublishSubscribeReceiver();
		receiver1.initialize();
		final PublishSubscribeReceiver receiver2 = new PublishSubscribeReceiver();
		receiver2.initialize();

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				receiver1.receive("pubsub_queue1");
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				receiver2.receive("pubsub_queue2");
			}
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		receiver1.destroy();
		receiver2.destroy();
	}
}

