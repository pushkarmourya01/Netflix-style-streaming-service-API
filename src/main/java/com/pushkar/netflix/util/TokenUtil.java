package com.pushkar.netflix.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

	public String issueToken() {
		return UUID.randomUUID().toString();
	}

	public String hash(String rawPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encoded = digest.digest(rawPassword.getBytes());
			return HexFormat.of().formatHex(encoded);
		}
		catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("SHA-256 is not available", ex);
		}
	}

	public boolean matches(String rawPassword, String storedHash) {
		return MessageDigest.isEqual(hash(rawPassword).getBytes(), storedHash.getBytes());
	}

}