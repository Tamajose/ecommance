package com.webarch.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public KeyPair jwtKeyPair(
			@Value("${jwt.private-key:}") String privateKeyPem,
			@Value("${jwt.public-key:}") String publicKeyPem) {
		if (!privateKeyPem.isBlank() && !publicKeyPem.isBlank()) {
			return new KeyPair(parsePublicKey(publicKeyPem), parsePrivateKey(privateKeyPem));
		}
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			return generator.generateKeyPair();
		} catch (Exception e) {
			throw new IllegalStateException("Unable to generate RSA key pair", e);
		}
	}

	@Bean
	public RSAPublicKey jwtPublicKey(KeyPair jwtKeyPair) {
		return (RSAPublicKey) jwtKeyPair.getPublic();
	}

	@Bean
	public JwtEncoder jwtEncoder(KeyPair jwtKeyPair) {
		return NimbusJwtEncoder.withKeyPair(
						(RSAPublicKey) jwtKeyPair.getPublic(),
						(RSAPrivateKey) jwtKeyPair.getPrivate())
				.algorithm(SignatureAlgorithm.RS256)
				.jwkPostProcessor(b -> b.keyID("ecommance-rsa")
						.algorithm(com.nimbusds.jose.JWSAlgorithm.RS256))
				.build();
	}

	@Bean
	public JwtDecoder jwtDecoder(RSAPublicKey jwtPublicKey) {
		return NimbusJwtDecoder.withPublicKey(jwtPublicKey).build();
	}

	private static RSAPublicKey parsePublicKey(String pem) {
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(decodePem(pem)));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid RSA public key PEM", e);
		}
	}

	private static RSAPrivateKey parsePrivateKey(String pem) {
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(decodePem(pem)));
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid RSA private key PEM", e);
		}
	}

	private static byte[] decodePem(String pem) {
		return Base64.getDecoder().decode(
				pem.replaceAll("-----BEGIN [A-Z ]+-----", "")
						.replaceAll("-----END [A-Z ]+-----", "")
						.replaceAll("\\s", ""));
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		authoritiesConverter.setAuthoritiesClaimName("scope");
		authoritiesConverter.setAuthorityPrefix("");
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		return converter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/**", "/api/users/register").permitAll()
						.requestMatchers("/.well-known/jwks.json").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers("/api/users", "/api/users/**").hasAuthority("ADMIN")
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}
}
