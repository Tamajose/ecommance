package com.webarch.user.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;

@RestController
@RequestMapping("/.well-known")
public class JwksController {

	private final RSAPublicKey publicKey;

	public JwksController(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}

	@GetMapping(value = "/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> jwks() {
		RSAKey jwk = new RSAKey.Builder(publicKey)
				.keyID("ecommance-rsa")
				.algorithm(JWSAlgorithm.RS256)
				.build();
		return ResponseEntity.ok(new JWKSet(jwk).toString());
	}
}
