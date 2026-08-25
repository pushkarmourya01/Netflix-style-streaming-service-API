package com.pushkar.netflix.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.pushkar.netflix.util.TokenStore;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

	@Mock
	private TokenStore tokenStore;

	private AuthFilter authFilter;

	@BeforeEach
	void setUp() {
		authFilter = new AuthFilter(tokenStore);
	}

	@Test
	void shouldSkipPublicMoviePaths() throws IOException, ServletException {
		MockFilterChain chain = new MockFilterChain();

		authFilter.doFilter(new MockHttpServletRequest("GET", "/api/movies"), new MockHttpServletResponse(), chain);

		assertThat(chain.getRequest()).isNotNull();
	}

	@Test
	void shouldSkipPublicAuthPaths() throws IOException, ServletException {
		MockFilterChain chain = new MockFilterChain();

		authFilter.doFilter(new MockHttpServletRequest("POST", "/api/auth/login"), new MockHttpServletResponse(), chain);

		assertThat(chain.getRequest()).isNotNull();
	}

	@Test
	void shouldRejectRequestWithoutToken() throws IOException, ServletException {
		MockHttpServletResponse response = new MockHttpServletResponse();

		authFilter.doFilter(new MockHttpServletRequest("GET", "/api/subscription"), response, new MockFilterChain());

		assertThat(response.getStatus()).isEqualTo(401);
	}

	@Test
	void shouldRejectInvalidToken() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/subscription");
		request.addHeader("Authorization", "Bearer invalid-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(tokenStore.isValid("invalid-token")).thenReturn(false);

		authFilter.doFilter(request, response, new MockFilterChain());

		assertThat(response.getStatus()).isEqualTo(401);
	}

	@Test
	void shouldExposeAuthenticatedEmailForValidToken() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/subscription");
		request.addHeader("Authorization", "Bearer valid-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		when(tokenStore.isValid("valid-token")).thenReturn(true);
		when(tokenStore.emailOf("valid-token")).thenReturn("pushkar@example.com");

		authFilter.doFilter(request, response, chain);

		assertThat(chain.getRequest()).isNotNull();
		assertThat(AuthFilter.authenticatedEmail((MockHttpServletRequest) chain.getRequest()))
			.isEqualTo("pushkar@example.com");
	}

}