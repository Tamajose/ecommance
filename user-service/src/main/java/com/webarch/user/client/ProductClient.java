package com.webarch.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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

	public void removeListing(Long productId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Internal-Api-Key", internalApiKey);
		RequestEntity<Void> request = RequestEntity
				.delete(URI.create(productServiceUrl + "/api/products/" + productId + "/moderate"))
				.headers(headers)
				.build();
		restTemplate.exchange(request, Void.class);
	}
}
