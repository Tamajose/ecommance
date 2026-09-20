package com.webarch.user.service;

import com.webarch.user.domain.Role;
import com.webarch.user.domain.User;
import com.webarch.user.dto.AuthResponse;
import com.webarch.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class GoogleAuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;

	public GoogleAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
	}

	@Transactional
	public AuthResponse loginWithGoogle(String email, String fullName) {
		if(email == null || email.isBlank()){
			throw new IllegalArgumentException("Google account did not provide an email address");
		}

		User user = userRepository.findByEmail(email)
				.orElseGet(() -> userRepository.save(newOAuthUser(email, fullName)));

		if(!user.isEnabled()){
			throw new IllegalArgumentException("Account is disabled: " + user.getUsername());
		}

		String scope = user.getRole().name();

		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("ecommance-user-service")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(3600))
				.subject(user.getUsername())
				.claim("scope", scope)
				.build();

		String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return new AuthResponse(token, user.getUsername(), scope);
	}

	private User newOAuthUser(String email, String fullName) {
		String base = email.substring(0, email.indexOf('@')).replaceAll("[^a-zA-Z0-9._-]", "");
		if(base.isBlank()){
			base = "user";
		}

		String username = base;
		int suffix = 1;
		while(userRepository.existsByUsername(username)){
			username = base + suffix++;
		}

		return User.builder()
				.username(username)
				.email(email)
				.password(passwordEncoder.encode(UUID.randomUUID().toString()))
				.name(fullName != null && !fullName.isBlank() ? fullName : username)
				.role(Role.BUYER)
				.build();
	}
}
