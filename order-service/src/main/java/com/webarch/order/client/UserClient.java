package com.webarch.order.client;

import com.webarch.order.dto.ShippingAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class UserClient {

	private final RestTemplate restTemplate;
	private final String userServiceUrl;
	private final String internalApiKey;

	public UserClient(RestTemplate restTemplate,
			@Value("${user.service.url}") String userServiceUrl,
			@Value("${internal.api-key}") String internalApiKey) {
		this.restTemplate = restTemplate;
		this.userServiceUrl = userServiceUrl;
		this.internalApiKey = internalApiKey;
	}

	public void syncProfileAddress(String username, ShippingAddress address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Internal-Api-Key", internalApiKey);
		RequestEntity<ProfileAddressSyncRequest> request = RequestEntity
				.patch(URI.create(userServiceUrl + "/api/users/profile-address"))
				.headers(headers)
				.body(new ProfileAddressSyncRequest(username, address));
		restTemplate.exchange(request, Void.class);
	}

	private record ProfileAddressSyncRequest(String username, ShippingAddress address) {
	}
}
