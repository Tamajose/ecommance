package com.webarch.user.service;

import com.webarch.user.dto.AuthRequest;
import com.webarch.user.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtEncoder jwtEncoder;

	public AuthService(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
	}

	public AuthResponse authenticate(AuthRequest request) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password())
		);

		String scope = auth.getAuthorities().stream()
				.map(a -> a.getAuthority())
				.collect(Collectors.joining(" "));

		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("ecommance-user-service")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(3600))
				.subject(auth.getName())
				.claim("scope", scope)
				.build();

		String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return new AuthResponse(token, auth.getName(), scope);
	}
}