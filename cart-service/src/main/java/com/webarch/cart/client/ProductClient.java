package com.webarch.cart.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

	private final RestTemplate restTemplate;
	private final String productServiceUrl;

	public ProductClient(RestTemplate restTemplate, @Value("${product.service.url}") String productServiceUrl) {
		this.restTemplate = restTemplate;
		this.productServiceUrl = productServiceUrl;
	}

	public ProductSnapshot getProduct(Long id) {
		return restTemplate.getForObject(productServiceUrl + "/api/products/" + id, ProductSnapshot.class);
	}

	public record ProductSnapshot(Long id, String name, BigDecimal price, Integer stockQuantity) {
	}
}
