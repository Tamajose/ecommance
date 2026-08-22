package com.webarch.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ProductClient {

	private final RestTemplate restTemplate;
	private final String productServiceUrl;
	private final String internalApiKey;

	public ProductClient(RestTemplate restTemplate,
			@Value("${product.service.url}") String productServiceUrl,
			@Value("${internal.api-key}") String internalApiKey) {
		this.restTemplate = restTemplate;
		this.productServiceUrl = productServiceUrl;
		this.internalApiKey = internalApiKey;
	}

	public ProductSnapshot getProduct(Long id) {
		return restTemplate.getForObject(productServiceUrl + "/api/products/" + id, ProductSnapshot.class);
	}

	public void adjustStock(Long id, int delta) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Internal-Api-Key", internalApiKey);
		RequestEntity<StockRequest> request = RequestEntity
				.post(java.net.URI.create(productServiceUrl + "/api/products/" + id + "/stock"))
				.headers(headers)
				.body(new StockRequest(delta));
		restTemplate.exchange(request, Void.class);
	}

	public record ProductSnapshot(Long id, String name, BigDecimal price, Integer stockQuantity) {
	}

	public record StockRequest(int delta) {
	}
}
