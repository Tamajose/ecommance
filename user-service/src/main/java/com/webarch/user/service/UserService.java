package com.webarch.user.service;

import com.webarch.user.domain.Role;
import com.webarch.user.domain.User;
import com.webarch.user.dto.AddressRequest;
import com.webarch.user.dto.UserRequest;
import com.webarch.user.dto.UserResponse;
import com.webarch.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserResponse createUser(UserRequest request) {
		if (userRepository.existsByUsername(request.username())) {
			throw new IllegalArgumentException("Username already taken");
		}
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already registered");
		}

		User user = User.builder()
				.username(request.username())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.name(request.name())
				.phone(request.phone())
				.role(request.role() != null ? request.role() : Role.USER)
				.build();

		User saved = userRepository.save(user);
		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public UserResponse getUserById(Long id) {
		return userRepository.findById(id)
				.map(this::toResponse)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
	}

	@Transactional
	public UserResponse updateAddress(Long id, AddressRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		user.setAddressLine(request.line());
		user.setAddressCity(request.city());
		user.setAddressPostalCode(request.postalCode());
		user.setAddressCountry(request.country());
		return toResponse(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities("ROLE_" + user.getRole().name())
				.disabled(!user.isEnabled())
				.build();
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getName(),
				user.getPhone(),
				user.getRole(),
				user.isEnabled(),
				user.getCreatedAt(),
				user.getAddressLine1(),
				user.getAddressLine2(),
				user.getAddressCity(),
				user.getAddressState(),
				user.getAddressPostalCode(),
				user.getAddressCountry()
		);
	}
}
