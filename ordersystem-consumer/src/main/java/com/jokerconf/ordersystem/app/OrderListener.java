package com.jokerconf.ordersystem.app;

import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jokerconf.ordersystem.model.Order;

public class OrderListener {

	public void receiveMessage(Object message) {
		try {
			Order order = (Order) SerializationUtils.deserialize((byte[]) message);
			System.out.println("Order received: " + order.getType());
			System.out.println("Order price: " + order.getPrice());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		//
		// byte[] messageBytes = (byte[]) message;
		// System.out.println(messageBytes.length);
		// Object obj = SerializationUtils.deserialize(messageBytes);
		// System.out.println(obj);
		//
		// try (ByteArrayInputStream b = new ByteArrayInputStream((byte[])
		// message)) {
		// try (ObjectInputStream o = new ObjectInputStream(b)) {
		// System.out.println(o.readObject());
		// System.out.println(o.readObject().getClass());
		// }
		// } catch (ClassNotFoundException e1) {
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
	}

	// public void receiveMessage(byte[] message) {
	// System.out.println("Message received: " + message.length);
	// //
	// // byte[] messageBytes = (byte[]) message;
	// // System.out.println(messageBytes.length);
	// // Object obj = SerializationUtils.deserialize(messageBytes);
	// // System.out.println(obj);
	// //
	// // try (ByteArrayInputStream b = new ByteArrayInputStream((byte[])
	// // message)) {
	// // try (ObjectInputStream o = new ObjectInputStream(b)) {
	// // System.out.println(o.readObject());
	// // System.out.println(o.readObject().getClass());
	// // }
	// // } catch (ClassNotFoundException e1) {
	// // e1.printStackTrace();
	// // } catch (IOException e1) {
	// // e1.printStackTrace();
	// // }
	// }
	//
	// public void receiveMessage(Order message) {
	// System.out.println("Message received: " + message.getType());
	// //
	// // byte[] messageBytes = (byte[]) message;
	// // System.out.println(messageBytes.length);
	// // Object obj = SerializationUtils.deserialize(messageBytes);
	// // System.out.println(obj);
	// //
	// // try (ByteArrayInputStream b = new ByteArrayInputStream((byte[])
	// // message)) {
	// // try (ObjectInputStream o = new ObjectInputStream(b)) {
	// // System.out.println(o.readObject());
	// // System.out.println(o.readObject().getClass());
	// // }
	// // } catch (ClassNotFoundException e1) {
	// // e1.printStackTrace();
	// // } catch (IOException e1) {
	// // e1.printStackTrace();
	// // }
	// }

	public static void main(String[] args) {
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("configuration.xml");
	}
}
