package com.pushkar.netflix.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pushkar.netflix.dto.SubscriptionRequest;
import com.pushkar.netflix.dto.SubscriptionResponse;
import com.pushkar.netflix.entity.SubscriptionPlan;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.SubscriptionRepository;

class SubscriptionServiceTest {

	private SubscriptionRepository subscriptionRepository;

	private SubscriptionService subscriptionService;

	@BeforeEach
	void setUp() {
		subscriptionRepository = new SubscriptionRepository();
		subscriptionService = new SubscriptionService(subscriptionRepository);
	}

	private static final String EMAIL = "pushkar@example.com";

	@Test
	void shouldCreateSubscriptionForNewEmail() {
		SubscriptionResponse response = subscriptionService.subscribe(EMAIL, new SubscriptionRequest(SubscriptionPlan.PREMIUM));

		assertThat(response.email()).isEqualTo(EMAIL);
		assertThat(response.plan()).isEqualTo(SubscriptionPlan.PREMIUM);
		assertThat(response.active()).isTrue();
		assertThat(response.priceInRupees()).isEqualTo(499);
		assertThat(response.screenCount()).isEqualTo(4);
	}

	@Test
	void shouldUpgradeExistingSubscription() {
		subscriptionService.subscribe(EMAIL, new SubscriptionRequest(SubscriptionPlan.BASIC));

		SubscriptionResponse upgraded = subscriptionService.subscribe(EMAIL, new SubscriptionRequest(SubscriptionPlan.FAMILY));

		assertThat(upgraded.plan()).isEqualTo(SubscriptionPlan.FAMILY);
		assertThat(upgraded.priceInRupees()).isEqualTo(799);
		assertThat(subscriptionRepository.findByEmail(EMAIL)).isPresent();
	}

	@Test
	void shouldReturnCurrentPlan() {
		subscriptionService.subscribe(EMAIL, new SubscriptionRequest(SubscriptionPlan.BASIC));

		assertThat(subscriptionService.currentPlan(EMAIL).plan()).isEqualTo(SubscriptionPlan.BASIC);
	}

	@Test
	void shouldCancelSubscription() {
		subscriptionService.subscribe(EMAIL, new SubscriptionRequest(SubscriptionPlan.PREMIUM));

		assertThat(subscriptionService.cancel(EMAIL).active()).isFalse();
	}

	@Test
	void shouldThrowWhenNoSubscriptionExists() {
		assertThatThrownBy(() -> subscriptionService.currentPlan(EMAIL)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void shouldThrowWhenCancellingWithoutSubscription() {
		assertThatThrownBy(() -> subscriptionService.cancel(EMAIL)).isInstanceOf(ResourceNotFoundException.class);
	}

}