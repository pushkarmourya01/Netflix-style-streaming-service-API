package com.pushkar.netflix.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pushkar.netflix.dto.AuthRequest;
import com.pushkar.netflix.dto.AuthResponse;
import com.pushkar.netflix.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	private AuthController authController;

	@BeforeEach
	void setUp() {
		authController = new AuthController(authService);
	}

	@Test
	void shouldRegisterWithCreatedStatus() {
		AuthRequest request = new AuthRequest("pushkar@example.com", "secret123");
		AuthResponse response = new AuthResponse("pushkar@example.com", "token-1", LocalDateTime.now());
		when(authService.register(request)).thenReturn(response);

		ResponseEntity<AuthResponse> result = authController.register(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getBody()).isEqualTo(response);
	}

	@Test
	void shouldLoginWithOkStatus() {
		AuthRequest request = new AuthRequest("pushkar@example.com", "secret123");
		AuthResponse response = new AuthResponse("pushkar@example.com", "token-2", LocalDateTime.now());
		when(authService.login(request)).thenReturn(response);

		ResponseEntity<AuthResponse> result = authController.login(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().token()).isEqualTo("token-2");
	}

}