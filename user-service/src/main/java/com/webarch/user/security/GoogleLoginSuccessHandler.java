package com.webarch.user.security;

import com.webarch.user.dto.AuthResponse;
import com.webarch.user.service.GoogleAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GoogleLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final GoogleAuthService googleAuthService;
	private final String frontendUrl;

	public GoogleLoginSuccessHandler(GoogleAuthService googleAuthService,
			@Value("${app.frontend-url}") String frontendUrl) {
		this.googleAuthService = googleAuthService;
		this.frontendUrl = frontendUrl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		if (!(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
			response.sendRedirect(frontendUrl + "/login?error=google");
			return;
		}

		String email = oidcUser.getEmail();
		if (email == null || email.isBlank()) {
			response.sendRedirect(frontendUrl + "/login?error=google_email");
			return;
		}

		AuthResponse auth = googleAuthService.loginWithGoogle(email, oidcUser.getFullName());

		// Token travels in the URL *fragment*: fragments are never sent to a server,
		// so it stays out of access logs, proxy logs and the Referer header.
		String target = frontendUrl + "/login/callback"
				+ "#token=" + encode(auth.token())
				+ "&username=" + encode(auth.username())
				+ "&role=" + encode(auth.role());

		response.sendRedirect(target);
	}

	private static String encode(String value) {
		return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
	}
}
