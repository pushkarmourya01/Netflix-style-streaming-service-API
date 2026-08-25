package com.pushkar.netflix.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenUtilTest {

	private TokenUtil tokenUtil;

	@BeforeEach
	void setUp() {
		tokenUtil = new TokenUtil();
	}

	@Test
	void shouldIssueNonBlankToken() {
		String token = tokenUtil.issueToken();

		assertThat(token).isNotBlank();
	}

	@Test
	void shouldIssueUniqueTokenEachTime() {
		String first = tokenUtil.issueToken();
		String second = tokenUtil.issueToken();

		assertThat(first).isNotEqualTo(second);
	}

	@Test
	void shouldHashPasswordDeterministically() {
		String hash = tokenUtil.hash("password123");

		assertThat(hash).hasSize(64);
		assertThat(hash).isEqualTo(tokenUtil.hash("password123"));
	}

	@Test
	void shouldProduceDifferentHashesForDifferentPasswords() {
		assertThat(tokenUtil.hash("password123")).isNotEqualTo(tokenUtil.hash("password124"));
	}

	@Test
	void shouldMatchCorrectPassword() {
		assertThat(tokenUtil.matches("password123", tokenUtil.hash("password123"))).isTrue();
	}

	@Test
	void shouldRejectWrongPassword() {
		assertThat(tokenUtil.matches("wrong-password", tokenUtil.hash("password123"))).isFalse();
	}

}