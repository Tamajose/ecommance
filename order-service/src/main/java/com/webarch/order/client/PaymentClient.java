package com.webarch.order.client;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.webarch.order.domain.PaymentMethod;

@Component
public class PaymentClient {

	private final RestTemplate restTemplate;
	private final String paymentServiceUrl;
	private final String internalApiKey;

	public PaymentClient(RestTemplate restTemplate,
			@Value("${payment.service.url}") String paymentServiceUrl,
			@Value("${internal.api-key}") String internalApiKey) {
		this.restTemplate = restTemplate;
		this.paymentServiceUrl = paymentServiceUrl;
		this.internalApiKey = internalApiKey;
	}

	public Long createPayment(String username, Long orderId, BigDecimal amount, PaymentMethod paymentMethod) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Internal-Api-Key", internalApiKey);
		RequestEntity<PaymentRequest> request = RequestEntity
				.post(URI.create(paymentServiceUrl + "/api/payments"))
				.headers(headers)
				.body(new PaymentRequest(username, orderId, amount, paymentMethod));
		PaymentResponse response = restTemplate.exchange(request, PaymentResponse.class).getBody();
		return response == null ? null : response.paymentId();
	}

	public record PaymentRequest(String username, Long orderId, BigDecimal amount, PaymentMethod paymentMethod) {
	}

	public record PaymentResponse(Long paymentId) {
	}
}
