package com.jokerconf.rabbitmq.spring.beans;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.Encoded;
import javax.ws.rs.core.MediaType;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitTemplateExample {
	
	public static void main(String[] args) throws UnsupportedEncodingException {

		CachingConnectionFactory factory = null;
		try {
			factory = new CachingConnectionFactory("localhost");
			RabbitTemplate template = new RabbitTemplate(factory);
			
			MessageProperties properties = new MessageProperties();
			properties.setContentType("text/xml");
			properties.setContentEncoding("utf-8");
			
			String soapMessage = "<soapenv:Envelope" + 
	                  "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope\">\n" +  
	                  "<soapenv:Header/>\n" +
	                  "<soapenv:Body>\n" +
	                  "  <p:greet xmlns:p=\"http://greet.service.kishanthan.org\">\n" + 
	                  "     <in>" + "sample" + "</in>\n" +
	                  "  </p:greet>\n" +
	                  "</soapenv:Body>\n" +
	                  "</soapenv:Envelope>";
	                  
			Message message = new Message(soapMessage.getBytes("UTF-8"), properties);
			template.convertAndSend("", "test-queue", message);
		} finally {
			if(factory != null) {
				factory.destroy();
			}
		}
	}
}
