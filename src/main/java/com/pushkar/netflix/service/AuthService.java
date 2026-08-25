package com.pushkar.netflix.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pushkar.netflix.dto.AuthRequest;
import com.pushkar.netflix.dto.AuthResponse;
import com.pushkar.netflix.entity.User;
import com.pushkar.netflix.exception.DuplicateResourceException;
import com.pushkar.netflix.exception.InvalidCredentialsException;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.UserRepository;
import com.pushkar.netflix.util.TokenStore;
import com.pushkar.netflix.util.TokenUtil;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final TokenUtil tokenUtil;
	private final TokenStore tokenStore;

	public AuthService(UserRepository userRepository, TokenUtil tokenUtil, TokenStore tokenStore) {
		this.userRepository = userRepository;
		this.tokenUtil = tokenUtil;
		this.tokenStore = tokenStore;
	}

	public AuthResponse register(AuthRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new DuplicateResourceException("User already registered with email: " + request.email());
		}

		User user = new User();
		user.setEmail(request.email().toLowerCase());
		user.setPassword(tokenUtil.hash(request.password()));
		user.setRegisteredAt(LocalDateTime.now());

		User saved = userRepository.save(user);

		String token = tokenUtil.issueToken();
		tokenStore.save(token, saved.getEmail());

		return new AuthResponse(saved.getEmail(), token, saved.getRegisteredAt());
	}

	public AuthResponse login(AuthRequest request) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new ResourceNotFoundException("No user found with email: " + request.email()));

		if (!tokenUtil.matches(request.password(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid password for email: " + request.email());
		}

		String token = tokenUtil.issueToken();
		tokenStore.save(token, user.getEmail());

		return new AuthResponse(user.getEmail(), token, user.getRegisteredAt());
	}

}