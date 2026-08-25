package com.pushkar.netflix.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pushkar.netflix.util.TokenStore;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AuthFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";
	public static final String EMAIL_ATTRIBUTE = "authenticatedEmail";
	private static final List<String> PUBLIC_PATHS = List.of("/api/auth", "/api/movies");

	private final TokenStore tokenStore;

	public AuthFilter(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return PUBLIC_PATHS.stream().anyMatch(request.getRequestURI()::startsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String token = extractToken(request);

		if (token == null || !tokenStore.isValid(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("{\"title\":\"Unauthorized\",\"detail\":\"A valid Bearer token is required\"}");
			return;
		}

		request.setAttribute(EMAIL_ATTRIBUTE, tokenStore.emailOf(token));
		chain.doFilter(request, response);
	}

	public static String authenticatedEmail(HttpServletRequest request) {
		return (String) request.getAttribute(EMAIL_ATTRIBUTE);
	}

	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith(BEARER_PREFIX)) {
			return null;
		}
		return header.substring(BEARER_PREFIX.length()).trim();
	}

}