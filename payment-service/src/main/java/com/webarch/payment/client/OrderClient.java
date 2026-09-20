package com.webarch.payment.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Component
public class OrderClient {

	private final RestTemplate restTemplate;
	private final String orderServiceUrl;
	private final String internalApiKey;

	public OrderClient(RestTemplate restTemplate,
			@Value("${order.service.url}") String orderServiceUrl,
			@Value("${internal.api-key}") String internalApiKey) {
		this.restTemplate = restTemplate;
		this.orderServiceUrl = orderServiceUrl;
		this.internalApiKey = internalApiKey;
	}

	public void syncOrderStatus(Long orderId, String paymentStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Internal-Api-Key", internalApiKey);
		RequestEntity<Map<String, String>> request = RequestEntity
				.post(URI.create(orderServiceUrl + "/api/orders/" + orderId + "/sync-payment-status"))
				.headers(headers)
				.body(Map.of("paymentStatus", paymentStatus));
		restTemplate.exchange(request, Void.class);
	}
}
