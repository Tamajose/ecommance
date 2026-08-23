package com.webarch.cart.client;

import com.webarch.cart.dto.ShippingAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class OrderClient {

	private final RestTemplate restTemplate;
	private final String orderServiceUrl;

	public OrderClient(RestTemplate restTemplate, @Value("${order.service.url}") String orderServiceUrl) {
		this.restTemplate = restTemplate;
		this.orderServiceUrl = orderServiceUrl;
	}

	public OrderResponse createOrder(String token, OrderRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		RequestEntity<OrderRequest> entity = RequestEntity
				.post(URI.create(orderServiceUrl + "/api/orders"))
				.headers(headers)
				.body(request);
		return restTemplate.exchange(entity, OrderResponse.class).getBody();
	}

	public record OrderItemRequest(Long productId, Integer quantity) {
	}

	public record OrderRequest(
			List<OrderItemRequest> items,
			String recipientName,
			ShippingAddress shippingAddress
	) {
	}

	public record OrderResponse(Long id) {
	}
}
