package com.webarch.user.service;

import com.webarch.user.domain.Address;
import com.webarch.user.domain.User;
import com.webarch.user.dto.AddressRequest;
import com.webarch.user.dto.AddressResponse;
import com.webarch.user.repository.AddressRepository;
import com.webarch.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public AddressResponse create(Long userId, AddressRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

		boolean isDefault = request.isDefault() || addressRepository.findByUserId(userId).isEmpty();

		Address address = Address.builder()
				.user(user)
				.line1(request.line1())
				.line2(request.line2())
				.city(request.city())
				.state(request.state())
				.postalCode(request.postalCode())
				.country(request.country())
				.isDefault(isDefault)
				.build();

		return toResponse(addressRepository.save(address));
	}

	@Transactional(readOnly = true)
	public List<AddressResponse> list(Long userId) {
		return addressRepository.findByUserId(userId).stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public AddressResponse get(Long userId, Long addressId) {
		return addressRepository.findByIdAndUserId(addressId, userId)
				.map(this::toResponse)
				.orElseThrow(() -> new IllegalArgumentException("Address not found"));
	}

	@Transactional
	public void delete(Long userId, Long addressId) {
		Address address = addressRepository.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Address not found"));
		addressRepository.delete(address);
	}

	private AddressResponse toResponse(Address a) {
		return new AddressResponse(
				a.getId(),
				a.getUser().getId(),
				a.getLine1(),
				a.getLine2(),
				a.getCity(),
				a.getState(),
				a.getPostalCode(),
				a.getCountry(),
				a.isDefault()
		);
	}
}