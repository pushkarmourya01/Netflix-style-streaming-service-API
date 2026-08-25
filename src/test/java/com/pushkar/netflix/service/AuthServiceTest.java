package com.pushkar.netflix.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pushkar.netflix.dto.AuthRequest;
import com.pushkar.netflix.dto.AuthResponse;
import com.pushkar.netflix.entity.User;
import com.pushkar.netflix.exception.DuplicateResourceException;
import com.pushkar.netflix.exception.InvalidCredentialsException;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.UserRepository;
import com.pushkar.netflix.util.TokenStore;
import com.pushkar.netflix.util.TokenUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenUtil tokenUtil;

	@Mock
	private TokenStore tokenStore;

	@InjectMocks
	private AuthService authService;

	private User savedUser(String email) {
		User user = new User();
		user.setId(1L);
		user.setEmail(email);
		user.setPassword("hashed-password");
		user.setRegisteredAt(LocalDateTime.now());
		return user;
	}

	@Test
	void shouldRegisterNewUser() {
		AuthRequest request = new AuthRequest("Pushkar@Example.com", "secret123");
		when(userRepository.existsByEmail("Pushkar@Example.com")).thenReturn(false);
		when(tokenUtil.hash("secret123")).thenReturn("hashed-password");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> savedUser("pushkar@example.com"));
		when(tokenUtil.issueToken()).thenReturn("token-1");

		AuthResponse response = authService.register(request);

		assertThat(response.email()).isEqualTo("pushkar@example.com");
		assertThat(response.token()).isEqualTo("token-1");
		verify(tokenStore).save("token-1", "pushkar@example.com");
	}

	@Test
	void shouldRejectDuplicateEmailOnRegister() {
		AuthRequest request = new AuthRequest("pushkar@example.com", "secret123");
		when(userRepository.existsByEmail("pushkar@example.com")).thenReturn(true);

		assertThatThrownBy(() -> authService.register(request)).isInstanceOf(DuplicateResourceException.class);
	}

	@Test
	void shouldLoginExistingUser() {
		AuthRequest request = new AuthRequest("pushkar@example.com", "secret123");
		when(userRepository.findByEmail("pushkar@example.com")).thenReturn(Optional.of(savedUser("pushkar@example.com")));
		when(tokenUtil.matches("secret123", "hashed-password")).thenReturn(true);
		when(tokenUtil.issueToken()).thenReturn("token-2");

		AuthResponse response = authService.login(request);

		assertThat(response.email()).isEqualTo("pushkar@example.com");
		assertThat(response.token()).isEqualTo("token-2");
		verify(tokenStore).save("token-2", "pushkar@example.com");
	}

	@Test
	void shouldRejectWrongPasswordOnLogin() {
		AuthRequest request = new AuthRequest("pushkar@example.com", "wrong-password");
		when(userRepository.findByEmail("pushkar@example.com")).thenReturn(Optional.of(savedUser("pushkar@example.com")));
		when(tokenUtil.matches("wrong-password", "hashed-password")).thenReturn(false);

		assertThatThrownBy(() -> authService.login(request)).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	void shouldRejectUnknownEmailOnLogin() {
		AuthRequest request = new AuthRequest("ghost@example.com", "secret123");
		when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> authService.login(request)).isInstanceOf(ResourceNotFoundException.class);
	}

}