package com.webarch.user.controller;

import com.webarch.user.dto.AddressRequest;
import com.webarch.user.dto.AddressResponse;
import com.webarch.user.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
public class AddressController {

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping
	public ResponseEntity<List<AddressResponse>> list(@PathVariable Long userId) {
		return ResponseEntity.ok(addressService.list(userId));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponse> get(@PathVariable Long userId, @PathVariable Long addressId) {
		return ResponseEntity.ok(addressService.get(userId, addressId));
	}

	@PostMapping
	public ResponseEntity<AddressResponse> create(@PathVariable Long userId,
			@Valid @RequestBody AddressRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(userId, request));
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long addressId) {
		addressService.delete(userId, addressId);
		return ResponseEntity.noContent().build();
	}
}
