package com.jokerconf.ordersystem.transformer;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jokerconf.ordersystem.model.Order;

@Component
public class OrderTransformer {

	// @Autowired
	// private ApplicationContext applicationContext;

	private static final String URL_TEMPLATE = "https://api.ocr.space/Parse/Image";

	@Transformer
	public Order transform(Order imageOrder) {

		String url = imageOrder.getUrl();
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext;
		try {
			sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			RestTemplate client = new RestTemplate(requestFactory);
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("apikey", "helloworld");
			map.add("isOverlayRequired", "true");
			map.add("language", "eng");
			map.add("url", url);
			
			String result = client.postForObject(URL_TEMPLATE, map, String.class);

			System.out.println(result);
			
			JSONObject obj = new JSONObject(result);
			JSONObject first = (JSONObject) obj.getJSONArray("ParsedResults").get(0);
			String text = first.getString("ParsedText");
			
			// remove spaces and new lines
			text = text.trim().replaceAll("[\r\n ]", "");
			
			// replace , with = for order parts
			text = text.replaceAll(",", "=");
			System.out.println(text);
			Order order = new Order();
			order.parseOrder(text);
			return order;
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		return null;
	}
}
