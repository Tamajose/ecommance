package com.webarch.user.config;

import com.webarch.user.security.GoogleLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2LoginSecurityConfig {

	private final GoogleLoginSuccessHandler googleLoginSuccessHandler;
	private final String frontendUrl;

	public OAuth2LoginSecurityConfig(GoogleLoginSuccessHandler googleLoginSuccessHandler,
			@Value("${app.frontend-url}") String frontendUrl) {
		this.googleLoginSuccessHandler = googleLoginSuccessHandler;
		this.frontendUrl = frontendUrl;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain oauth2LoginFilterChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/oauth2/**", "/login/oauth2/**")
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
				)
				.oauth2Login(oauth2 -> oauth2
						.successHandler(googleLoginSuccessHandler)
						.failureUrl(frontendUrl + "/login?error=google")
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

		return http.build();
	}
}
