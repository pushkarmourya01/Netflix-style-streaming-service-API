package com.pushkar.netflix.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenStore {

	private final Map<String, String> tokenToEmail = new ConcurrentHashMap<>();

	public void save(String token, String email) {
		tokenToEmail.put(token, email.toLowerCase());
	}

	public boolean isValid(String token) {
		return tokenToEmail.containsKey(token);
	}

	public String emailOf(String token) {
		return tokenToEmail.get(token);
	}

}